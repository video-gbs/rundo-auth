package com.runjian.rbac.service.login.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.runjian.rbac.constant.AppTypeConstant;
import com.runjian.rbac.domain.entity.AppInfo;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.domain.vo.system.HomeVO;
import com.runjian.rbac.domain.vo.system.SysAppInfoVO;
import com.runjian.rbac.service.login.HomeSevice;
import com.runjian.rbac.service.system.AppInfoService;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.rbac.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HomeServiceImpl
 * @Description 门户首页
 * @date 2023-02-08 周三 11:53
 */
@Slf4j
@Service
public class HomeServiceImpl implements HomeSevice {

    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public CommonResponse getIndex() {
        UserInfo userInfo = securityUtil.getUserInfo();
        long userId = userInfo.getId();
        log.debug("用户{}进行首页操作", userId);
        // 通过登录的用户查取该用户已授权的角色信息
        List<String> roleCodeList = securityUtil.getRoleList();
        if (CollUtil.isEmpty(roleCodeList)) {
            return CommonResponse.failure(BusinessErrorEnums.PERM_NOT_FOUND, "未对用户" + userId + "进行角色授权");
        }
        log.debug("用户角色列表:{}", JSONUtil.toJsonStr(roleCodeList));
        // 查取角色已经授权的应用
        List<AppInfo> roleAppInfoList = appInfoService.getAppByRoleCodelist(roleCodeList);
        if (CollUtil.isEmpty(roleAppInfoList)) {
            return CommonResponse.failure(BusinessErrorEnums.PERM_NOT_FOUND, "未对用户" + userId + "进行应用授权");
        }
        // 去重
        List<AppInfo> appInfoList = CollUtil.distinct(roleAppInfoList);
        log.debug("用户应用列表:{}", JSONUtil.toJsonStr(roleAppInfoList));
        // 分拣应用种类
        Map<Integer, List<SysAppInfoVO>> appTypeMap = appInfoList.stream().map(appInfo -> {
            SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
            BeanUtils.copyProperties(appInfo, sysAppInfoVO);
            return sysAppInfoVO;
        }).collect(Collectors.groupingBy(SysAppInfoVO::getAppType));
        List<SysAppInfoVO> appList = appTypeMap.getOrDefault(AppTypeConstant.FUNC, new ArrayList<>());
        List<SysAppInfoVO> configList = appTypeMap.getOrDefault(AppTypeConstant.CONF, new ArrayList<>());
        List<SysAppInfoVO> devOpsList = appTypeMap.getOrDefault(AppTypeConstant.DEV, new ArrayList<>());
        HomeVO homeVO = new HomeVO();
        homeVO.setAppList(appList);
        homeVO.setConfigList(configList);
        homeVO.setDevOpsList(devOpsList);
        return CommonResponse.success(homeVO);
    }
}
