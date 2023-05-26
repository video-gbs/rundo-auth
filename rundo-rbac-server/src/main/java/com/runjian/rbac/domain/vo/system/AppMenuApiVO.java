package com.runjian.rbac.domain.vo.system;

import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AppMenuApiVO
 * @Description 转换实体
 * @date 2023-02-15 周三 16:14
 */
@Data
public class AppMenuApiVO {

    private Long id;

    private String idStr;

    private Long pid;

    private String name;
}
