package com.runjian.rbac.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.dto.system.MoveVideoAreaDTO;
import com.runjian.rbac.domain.dto.system.VideoAreaDTO;
import com.runjian.rbac.domain.entity.RoleArea;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.domain.entity.VideoArea;
import com.runjian.rbac.domain.vo.system.VideoAreaVO;
import com.runjian.rbac.domain.vo.tree.VideoAreaTree;
import com.runjian.rbac.feign.ExpansionClient;
import com.runjian.rbac.mapper.UserInfoMapper;
import com.runjian.rbac.mapper.VideoAraeMapper;
import com.runjian.rbac.service.system.RoleAreaService;
import com.runjian.rbac.service.system.VideoAreaService;
import com.runjian.rbac.util.RoleIdUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.rbac.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 安保区域 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class VideoAreaServiceImpl extends ServiceImpl<VideoAraeMapper, VideoArea> implements VideoAreaService {

    @Autowired
    private VideoAraeMapper videoAraeMapper;

    @Autowired
    private ExpansionClient expansionClient;

    @Lazy
    @Autowired
    private RoleAreaService roleAreaService;

    @Autowired
    private RoleIdUtil roleIdUtil;

    @Autowired
    private SecurityUtil securityUtil;

    @Transactional
    @Override
    public VideoAreaVO save(VideoAreaDTO dto) {
        // 确认父级安全区域是否存在
        VideoArea prentInfo = videoAraeMapper.selectById(dto.getAreaPid());
        if (prentInfo == null) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "父级安全区域不存在");
        }
        VideoArea area = new VideoArea();
        area.setAreaName(dto.getAreaName());
        area.setAreaPid(dto.getAreaPid());
        area.setDescription(dto.getDescription());
        area.setLevel(prentInfo.getLevel() + 1);
        log.info("添加安防区域入库数据信息{}", JSONUtil.toJsonStr(area));
        videoAraeMapper.insert(area);
        VideoAreaVO videoAreaVO = findById(area.getId());
        BeanUtils.copyProperties(area, videoAreaVO);
        RoleArea roleArea = new RoleArea();
        roleArea.setAreaId(area.getId());
        roleArea.setRoleId(1L);
        roleAreaService.save(roleArea);
        return videoAreaVO;
    }

    @Transactional
    @Override
    public void modifyById(VideoAreaDTO dto) {
        VideoArea videoArea = new VideoArea();
        BeanUtils.copyProperties(dto, videoArea);
        videoAraeMapper.updateById(videoArea);
    }

    @Override
    public VideoAreaVO findById(Long id) {
        return videoAraeMapper.mySelectById(id);
    }

    @Transactional
    @Override
    public CommonResponse erasureById(Long id) {
        // 1.判断是否为根节点
        if (id == 1L) {
            return CommonResponse.failure(BusinessErrorEnums.DEFAULT_MEDIA_DELETE_ERROR);
        }
        // 2.确认当前需要删除的安防区域有无下级安防区域
        VideoArea videoArea = videoAraeMapper.selectById(id);
        List<VideoAreaVO> videoAreaChildren = findVideoAreaListById(id);
        int size = videoAreaChildren.size();
        for (int i = size - 1; i >= 0; i--) {
            VideoAreaVO area = videoAreaChildren.get(i);
            if (area.getId().equals(videoArea.getId())) {
                // 2-1.剔除自己之后确认是否还存在子节点
                videoAreaChildren.remove(area);
            }
        }
        // 2-2.剔除自己之后确认是否还存在子节点
        if (CollUtil.isNotEmpty(videoAreaChildren)) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_AREA_OPERATION2);
        }
        // 3.调用远端确认是否可以删除
        CommonResponse<Boolean> commonResponse = expansionClient.videoAreaBindCheck(id);
        if (!commonResponse.getData()) {
            LambdaQueryWrapper<RoleArea> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RoleArea::getAreaId, id);
            roleAreaService.remove(wrapper);
            return CommonResponse.success(videoAraeMapper.deleteById(id));
        } else {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_AREA_OPERATION);
        }
    }

    @Override
    public void moveVideoArea(MoveVideoAreaDTO dto) {
        //  0-1 禁止本级移动到本级
        if (dto.getId().equals(dto.getAreaPid())) {
            log.info("禁止本级移动到本级");
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "禁止本级移动到本级");
        }
        // 目标位置的信息
        VideoArea targetInfo = videoAraeMapper.selectById(dto.getAreaPid());
        // 被移动项信息
        VideoArea info = videoAraeMapper.selectById(dto.getId());
        // 判断目标是否存在
        Optional.ofNullable(info).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "移动的节点不存在或已删除"));
        Optional.ofNullable(targetInfo).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "目标位置不存在或已删除"));

        // 1.如果目标位置与被移动项的父级ID相同，为同级别移动
        if (info.getAreaPid().equals(targetInfo.getAreaPid())) {
            log.info("同级别移动");
            // 切换排序顺序
            info.setAreaSort(targetInfo.getAreaSort());
            videoAraeMapper.updateById(targetInfo);
            targetInfo.setAreaSort(info.getAreaSort());
            videoAraeMapper.updateById(targetInfo);
            return;
        }
        // 2.如果目标位置与是移动项的子级，禁止移动
        List<VideoAreaVO> childrenList = findVideoAreaListById(dto.getId());
        List<Long> ids = childrenList.stream().map(VideoAreaVO::getId).collect(Collectors.toList());
        if (ids.contains(dto.getAreaPid())) {
            log.info("父级向子级移动");
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "禁止父级向子级移动");
            // return;
        }
        // 3.更新当前节点信息
        info.setAreaPid(dto.getAreaPid());
        info.setLevel(targetInfo.getLevel() + 1);
        videoAraeMapper.updateById(info);
        // 5.根据id，更新子节点层级信息
        updateChildren(info.getId(), info.getLevel());

    }

    private void updateChildren(Long id, Integer level) {
        List<VideoArea> infoList = getChildren(id);
        infoList.stream().peek(obj -> obj.setLevel(level + 1)).collect(Collectors.toList());
        updateBatchById(infoList);
        for (VideoArea videoArea : infoList) {
            updateChildren(videoArea.getId(), videoArea.getLevel());
        }
    }

    private List<VideoArea> getChildren(Long id) {
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VideoArea::getAreaPid, id);
        queryWrapper.orderBy(true, true, VideoArea::getAreaSort, VideoArea::getUpdatedTime);
        return videoAraeMapper.selectList(queryWrapper);
    }

    @Override
    public List<VideoAreaVO> findByList(Long areaId) {
        List<VideoAreaVO> videoAreaList = findVideoAreaListById(Objects.requireNonNullElse(areaId, 1L));
        return videoAreaList.stream().map(item -> {
            VideoAreaVO videoAreaVO = new VideoAreaVO();
            BeanUtils.copyProperties(item, videoAreaVO);
            return videoAreaVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<VideoArea> getVideoAreaByRoleCode(String roleCode) {
        return videoAraeMapper.selectVideoAreaByRoleCode(roleCode);
    }

    @Override
    public List<Long> getAreaIdListByRoleId(Long id) {
        return videoAraeMapper.selectAreaIdListByRoleId(id);
    }

    @Override
    public List<Tree<Long>> findByTree() {
        List<Long> roleIds = roleIdUtil.getRoleIdList();
        if(CollectionUtil.isEmpty(roleIds)){
            return null;
        }
        log.info("{}",JSONUtil.toJsonStr(roleIds));
        LambdaQueryWrapper<RoleArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleArea::getRoleId, roleIds);
        List<Long> roleAreaIds = roleAreaService.list(queryWrapper).stream().map(RoleArea::getAreaId).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(roleAreaIds)){
            // 授权的安防区域为空，直接返回，不进行后续操作
            return null;
        }
        List<VideoAreaVO> videoList = videoAraeMapper.selectAreaList(roleAreaIds);
        videoList.stream().distinct();
        List<VideoAreaTree> videoAreaTreeList = videoList.stream().map(item -> {
            VideoAreaTree bean = new VideoAreaTree();
            BeanUtils.copyProperties(item, bean);
            return bean;
        }).collect(Collectors.toList());
        LambdaQueryWrapper<VideoArea> videoAreaLambdaQueryWrapper = new LambdaQueryWrapper<>();
        UserInfo userInfo = securityUtil.getUserInfo();
        videoAreaLambdaQueryWrapper.eq(VideoArea::getCreatedBy, userInfo.getId());
        List<VideoArea> videoAreaList = videoAraeMapper.selectList(videoAreaLambdaQueryWrapper);
        videoAreaTreeList.addAll(videoAreaList.stream().map(item ->{
            VideoAreaTree bean = new VideoAreaTree();
            BeanUtils.copyProperties(item, bean);
            return bean;
        }).collect(Collectors.toList()));
        videoAreaTreeList.stream().distinct();
        List<TreeNode<Long>> nodeList = new ArrayList<>();
        videoAreaTreeList.forEach(e -> {
            TreeNode<Long> treeNode = new TreeNode<>(e.getId(), e.getParentId(), e.getAreaName(), e.getAreaSort());
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("level", e.getLevel());
            extraMap.put("description", e.getDescription());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        });
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig() {{
            setIdKey("id");
            setNameKey("areaName");
            setParentIdKey("areaPid");
            setChildrenKey("children");
            setWeightKey("areaSort");
        }};
        return TreeUtil.build(nodeList, 0L, treeNodeConfig, new DefaultNodeParser<>());
    }

    @Override
    public List<String> getAreaNameByUserId(Long userId) {
        return videoAraeMapper.selectAreaNameByUserId(userId);
    }

    private List<VideoAreaVO> findVideoAreaListById(Long id) {
        return videoAraeMapper.mySelectListById(id);
    }
}
