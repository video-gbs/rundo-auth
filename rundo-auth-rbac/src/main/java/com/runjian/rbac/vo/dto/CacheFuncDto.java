package com.runjian.rbac.vo.dto;

import com.runjian.rbac.entity.relation.FuncResourceRel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/6/7 14:24
 */
@Data
public class CacheFuncDto {

    /**
     * 服务名称
     */
    private String scope;

    /**
     * 功能名称
     */
    private String funcName;

    /**
     * 角色数组
     */
    private List<Long> roleIds = new ArrayList<>();

    /**
     * 资源组
     */
    private List<FuncResourceData> funcResourceDataList = new ArrayList<>();


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FuncResourceData{

        /**
         * 资源组
         */
        private String resourceKey;

        /**
         * 校验的参数
         */
        private String validateParam;

        /**
         * 根据关系数据创建
         * @param funcResourceRel 功能资源缓存数据
         * @return FuncResourceData
         */
        public FuncResourceData (FuncResourceRel funcResourceRel){
            this.resourceKey = funcResourceRel.getResourceKey();
            this.validateParam = funcResourceRel.getValidateParam();
        }

    }
}
