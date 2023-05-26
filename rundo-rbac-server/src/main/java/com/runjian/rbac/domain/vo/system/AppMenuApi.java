package com.runjian.rbac.domain.vo.system;

import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AppMenuApiVO
 * @Description 应用菜单接口响应实体
 * @date 2023-02-15 周三 14:15
 */
@Data
public class AppMenuApi {

    private Long appId;

    private String appName;

    private String appType;

    private Long menuId;

    private String menuPid;

    private String title;

    private String menuApp;

    private Long apiId;

    private String apiName;

    private String apiPid;
}
