package com.runjian.rbac.vo.dto;

import com.runjian.rbac.entity.relation.FuncResourceRel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

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
        private Integer enableMultiCheck;

        /**
         * 根据关系数据创建
         * @param funcResourceRel 功能资源缓存数据
         * @return FuncResourceData
         */
        public FuncResourceData (FuncResourceRel funcResourceRel){
            this.resourceKey = funcResourceRel.getResourceKey();
            this.validateParam = funcResourceRel.getValidateParam();
            this.enableMultiCheck = funcResourceRel.getEnableMultiCheck();
        }

        @Override
        public boolean equals(Object o){
            if (o instanceof FuncResourceData funcResourceData){
                if (funcResourceData.getResourceKey().equals(this.resourceKey)){
                    if(Objects.nonNull(funcResourceData.validateParam) && Objects.nonNull(this.validateParam)){
                        return  funcResourceData.getEnableMultiCheck().equals(this.enableMultiCheck);
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode(){
            int result = resourceKey.hashCode();
            if (Objects.nonNull(validateParam)){
                return 17 * result + validateParam.hashCode();
            }
            return result;
        }

    }
}
