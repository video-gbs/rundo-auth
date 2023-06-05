package com.runjian.rbac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.dao.UserMapper;
import com.runjian.rbac.dao.relation.UserRoleMapper;
import com.runjian.rbac.entity.SectionInfo;
import com.runjian.rbac.entity.UserInfo;
import com.runjian.rbac.entity.relation.UserRoleRel;
import com.runjian.rbac.service.DataBaseService;
import com.runjian.rbac.service.UserService;
import com.runjian.rbac.vo.response.GetUserPageRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Miracle
 * @date 2023/6/1 14:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final DataBaseService dataBaseService;

    private final UserRoleMapper userRoleMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public PageInfo<GetUserPageRsp> getUserPage(int page, int num, Long roleId, String username, Boolean isBinding) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(userMapper.selectByInRoleIdAndUsername(roleId, username, isBinding));
    }

    @Override
    public PageInfo<GetUserPageRsp> getUserPage(int page, int num, Long sectionId, String username, String workName, Boolean isInclude) {
        SectionInfo sectionInfo = dataBaseService.getSectionInfo(sectionId);
        PageHelper.startPage(page, num);
        List<GetUserPageRsp> getUserPageRspList;
        if (isInclude){
            getUserPageRspList = userMapper.selectAllUserBySectionLevelLikeAndUsernameAndWorkName(sectionInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + sectionInfo.getId(), username, workName);
        }else {
            getUserPageRspList = userMapper.selectAllUserBySectionIdAndUsernameAndWorkName(sectionInfo.getId(), username, workName);
        }
        return new PageInfo<>(getUserPageRspList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disabled(String authUser, Long userId, Integer disabled) {
        UserInfo userInfo = dataBaseService.getUserInfo(userId);
        if (userInfo.getDisabled().equals(disabled)){
            return;
        }
        userInfo.setDisabled(disabled);
        userInfo.setUpdateTime(LocalDateTime.now());
        userMapper.updateDisabled(userInfo);
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "用户服务", "执行禁用用户成功", String.format("用户'%s' 执行禁启用 用户'%s', 状态:'%s'", authUser, userInfo.getUsername(), disabled));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(String authUser, String username, String password, Long sectionId, LocalDateTime expiryStartTime, LocalDateTime expiryEndTime, String workName, String workNum, String address, String phone, String description, Set<Long> roleIds) {
        Optional<UserInfo> userInfoOp = userMapper.selectByUsername(username);
        if (userInfoOp.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("用户名%s已存在，请重新填写", username));
        }
        if (!sectionId.equals(0L)){
            dataBaseService.getSectionInfo(sectionId);
        }
        LocalDateTime nowTime = LocalDateTime.now();
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(passwordEncoder.encode(password));
        userInfo.setSectionId(sectionId);
        userInfo.setExpiryStartTime(expiryStartTime);
        userInfo.setExpiryEndTime(expiryEndTime);
        userInfo.setWorkName(workName);
        userInfo.setWorkNum(workNum);
        userInfo.setAddress(address);
        userInfo.setPhone(phone);
        userInfo.setDescription(description);
        userInfo.setCreateTime(nowTime);
        userInfo.setUpdateTime(nowTime);
        userMapper.save(userInfo);

        if (roleIds.size() > 0){
            userRoleMapper.saveAll(userInfo.getId(), roleIds, authUser, nowTime);
        }
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "用户服务", "添加用户成功", String.format("用户'%s' 执行添加 用户'%s'", authUser, username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(String authUser, Long userId, LocalDateTime expiryEndTime, String password, Long sectionId, String workName, String workNum, String phone, String address, String description, Set<Long> roleIds) {
        UserInfo userInfo = dataBaseService.getUserInfo(userId);
        if (Objects.isNull(password)){
            userInfo.setPassword(null);
        }else {
            userInfo.setPassword(passwordEncoder.encode(password));
        }
        LocalDateTime nowTime = LocalDateTime.now();
        userInfo.setExpiryEndTime(expiryEndTime);
        userInfo.setSectionId(sectionId);
        userInfo.setWorkName(workName);
        userInfo.setWorkNum(workNum);
        userInfo.setPhone(phone);
        userInfo.setAddress(address);
        userInfo.setDescription(description);
        userInfo.setUpdateTime(nowTime);

        if (Objects.nonNull(roleIds)){
            if (roleIds.size() == 0){
                userRoleMapper.deleteAllByUserId(userId);
            }else {
                Set<Long> existRoleIds = userRoleMapper.selectRoleIdByUserId(userId);
                if (existRoleIds.size() > 0){
                    Set<Long> difference = new HashSet<>(existRoleIds);
                    // 求交集
                    difference.retainAll(roleIds);
                    // 移除交集获得被删除的数据
                    existRoleIds.removeAll(difference);
                    // 移除交集获得增加的数据
                    roleIds.removeAll(difference);
                    // 删除去除的角色
                    userRoleMapper.deleteAllByUserIdAndRoleIds(userId, existRoleIds);
                }
                // 保存新的角色
                if (roleIds.size() > 0){
                    userRoleMapper.saveAll(userInfo.getId(), roleIds, authUser, nowTime);
                }
            }
        }
        userMapper.update(userInfo);
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "用户服务", "修改用户成功", String.format("用户'%s' 执行更新 用户'%s'", authUser, userInfo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUser(String authUser, Set<Long> userIds) {
        if (userIds.size() == 0){
            return;
        }
        userMapper.batchUpdateDeleted(userIds, CommonEnum.DISABLE.getCode());
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "用户服务", "删除用户成功", String.format("用户'%s' 执行删除 用户'%s'", authUser, userIds));
    }

}
