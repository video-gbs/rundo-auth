package com.runjian.rbac.vo.response;

import lombok.Data;

/**
 * 字典分组返回体
 * @author Miracle
 * @date 2023/6/5 10:16
 */
@Data
public class GetDictGroupRsp {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组编码
     */
    private String groupCode;

    /**
     * 字典名称
     */
    private String itemName;

    /**
     * 字典值
     */
    private String itemValue;

}
