package com.runjian.rbac.domain.vo.tree;

import lombok.Data;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RoleAppInfo
 * @Description
 * @date 2023-02-13 周一 0:14
 */
@Data
public class RoleAppInfo {
    private String appId;

    private String appName;

    List<MenuInfoTree> menuInfo;

    List<RoleApiInfo> roleApiInfoList;

}
