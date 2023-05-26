package com.runjian.rbac.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.constant.MenuSortConstant;
import com.runjian.rbac.constant.MenuTypeConstant;
import com.runjian.rbac.constant.StatusConstant;
import com.runjian.rbac.domain.dto.page.PageSysAppInfoDTO;
import com.runjian.rbac.domain.dto.system.*;
import com.runjian.rbac.domain.entity.AppInfo;
import com.runjian.rbac.domain.entity.RoleApp;
import com.runjian.rbac.domain.vo.system.SysAppInfoVO;
import com.runjian.rbac.mapper.AppInfoMapper;
import com.runjian.rbac.service.system.ApiInfoService;
import com.runjian.rbac.service.system.AppInfoService;
import com.runjian.rbac.service.system.MenuInfoService;
import com.runjian.rbac.service.system.RoleAppService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 应用信息 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class AppInfoServiceImpl extends ServiceImpl<AppInfoMapper, AppInfo> implements AppInfoService {

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Lazy
    @Autowired
    private MenuInfoService menuInfoService;

    @Lazy
    @Autowired
    private ApiInfoService apiInfoService;

    @Lazy
    @Autowired
    private RoleAppService roleAppService;

    @Transactional
    @Override
    public void save(SysAppInfoDTO dto) {
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(dto, appInfo);
        appInfoMapper.insert(appInfo);

        RoleApp roleApp = new RoleApp();
        roleApp.setRoleId(1L);
        roleApp.setAppId(appInfo.getId());
        roleAppService.save(roleApp);
        // 向菜单表中插入一条虚拟根菜单，并将虚拟根挂在到菜单的默认根节点下
        SysMenuInfoDTO menuInfoDTO = new SysMenuInfoDTO();
        menuInfoDTO.setAppId(appInfo.getId());
        menuInfoDTO.setAppName(appInfo.getAppName());
        menuInfoDTO.setMenuPid(1L);
        menuInfoDTO.setMenuSort(MenuSortConstant.DEFAULT);
        menuInfoDTO.setMenuType(MenuTypeConstant.APP);
        menuInfoDTO.setPath(appInfo.getAppUrl());
        menuInfoDTO.setComponent(appInfo.getComponent());
        menuInfoDTO.setIcon(appInfo.getAppIcon());
        menuInfoDTO.setTitle(appInfo.getAppName());
        menuInfoDTO.setName(StrUtil.removePrefix("/", appInfo.getAppUrl()));
        menuInfoDTO.setRedirect(appInfo.getRedirect());
        menuInfoDTO.setStatus(StatusConstant.ENABLE);
        menuInfoDTO.setHidden(0);
        menuInfoService.save(menuInfoDTO);

        // 向接口表中插入一条虚拟应用的根接口并将虚拟根挂在到接口的默认根节点下
        SysApiInfoDTO apiInfoDTO = new SysApiInfoDTO();
        apiInfoDTO.setAppId(appInfo.getId());
        apiInfoDTO.setAppName(appInfo.getAppName());
        apiInfoDTO.setApiPid(1L);
        apiInfoDTO.setApiName(appInfo.getAppName());
        apiInfoDTO.setUrl(appInfo.getAppUrl());
        apiInfoDTO.setApiSort(1);
        apiInfoDTO.setStatus(0);
        apiInfoService.save(apiInfoDTO);

    }

    @Transactional
    @Override
    public void modifyById(SysAppInfoDTO dto) {
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(dto, appInfo);
        appInfoMapper.updateById(appInfo);
        // 修改菜单表中的虚拟根
    }

    @Transactional
    @Override
    public void erasureById(Long id) {
        LambdaQueryWrapper<RoleApp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleApp::getAppId, id);
        roleAppService.remove(wrapper);
        appInfoMapper.deleteById(id);
    }

    @Override
    public Page<SysAppInfoVO> findByPage(QuerySysAppInfoDTO dto) {
        PageSysAppInfoDTO page = new PageSysAppInfoDTO();
        page.setAppName(dto.getAppName());
        page.setAppIp(dto.getAppIp());
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }
        return appInfoMapper.MySelectPage(page);
    }

    @Transactional
    @Override
    public void modifyByStatus(StatusSysAppInfoDTO dto) {
        AppInfo appInfo = appInfoMapper.selectById(dto.getId());
        appInfo.setStatus(dto.getStatus());
        appInfoMapper.updateById(appInfo);
    }

    @Override
    public List<AppInfo> getAppByRoleCode(String roleCode) {
        return appInfoMapper.selectAppByRoleCode(roleCode);
    }

    @Override
    public List<AppInfo> getAppByRoleCodelist(List<String> roleCodeList) {
        return appInfoMapper.selectAppByRoleCodeList(roleCodeList);
    }

    @Override
    public List<Long> getAppIdListByRoleId(Long roleId) {
        return appInfoMapper.findAppIdListByRoleId(roleId);
    }

    @Override
    public SysAppInfoVO findById(Long id) {
        AppInfo appInfo = appInfoMapper.selectById(id);
        SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
        BeanUtils.copyProperties(appInfo, sysAppInfoVO);
        return sysAppInfoVO;
    }

    @Override
    public List<SysAppInfoVO> findByList() {
        List<SysAppInfoVO> sysAppInfoVOList = new ArrayList<>();
        List<AppInfo> appInfoList = appInfoMapper.selectList(null);
        for (AppInfo appInfo : appInfoList) {
            SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
            BeanUtils.copyProperties(appInfo, sysAppInfoVO);
            sysAppInfoVOList.add(sysAppInfoVO);
        }
        return sysAppInfoVOList;
    }


}
