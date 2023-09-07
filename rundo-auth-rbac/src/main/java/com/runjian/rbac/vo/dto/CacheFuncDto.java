package com.runjian.rbac.vo.dto;

import com.runjian.rbac.entity.relation.FuncResourceRel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * 是否禁用
     */
    private Integer disabled;

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
         * 是否启用多维校验
         */
        private String multiGroup;

        /**
         * 根据关系数据创建
         * @param funcResourceRel 功能资源缓存数据
         * @return FuncResourceData
         */
        public FuncResourceData (FuncResourceRel funcResourceRel){
            this.resourceKey = funcResourceRel.getResourceKey();
            this.validateParam = funcResourceRel.getValidateParam();
            this.multiGroup = funcResourceRel.getMultiGroup();
        }

        @Override
        public boolean equals(Object o){
            if (o == this){
                return true;
            }
            if (o instanceof FuncResourceData funcResourceData){
                if (!StringUtils.equals(funcResourceData.getResourceKey(), this.resourceKey)){
                    return false;
                }
                if (!StringUtils.equals(funcResourceData.getValidateParam(), this.validateParam)){
                    return false;
                }
                return StringUtils.equals(funcResourceData.getMultiGroup(), this.multiGroup);
            }
            return false;
        }


        @Override
        public int hashCode(){
            int result = this.resourceKey.hashCode();
            if (Objects.nonNull(this.validateParam)){
                result = 17 * result + this.validateParam.hashCode();
            }
            if (Objects.nonNull(this.multiGroup)){
                result = 17 * result + this.multiGroup.hashCode();
            }
            return result;
        }
    }
}
