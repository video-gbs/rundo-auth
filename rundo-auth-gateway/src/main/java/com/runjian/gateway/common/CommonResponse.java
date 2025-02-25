package com.runjian.gateway.common;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Miracle
 * @date 2020/3/3 22:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息码
     */
    private int code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 检查异常是否为指定类型异常
     */
    public Boolean isError(){
        return code != BusinessErrorEnums.SUCCESS.getErrCode();
    }

    /**
     * 如果有异常进行报错
     * @param businessErrorEnums
     */
    public void ifErrorThrowException(BusinessErrorEnums businessErrorEnums){
        if (isError()){
            throw new BusinessException(businessErrorEnums, msg);
        }
    }

    /**
     * 成功无数据返回
     *
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> success() {
        return success(null);
    }

    /**
     * 成功有数据返回
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> success(T data) {
        return create(BusinessErrorEnums.SUCCESS.getErrCode(), BusinessErrorEnums.SUCCESS.getErrMsg(), data);
    }

    /**
     * 失败自定义异常,无数据返回
     *
     * @param businessErrorEnums
     * @return
     */
    public static <T> CommonResponse<T> failure(BusinessErrorEnums businessErrorEnums) {
        return failure(businessErrorEnums, null);
    }

    /**
     * 失败自定义异常带数据
     *
     * @param businessErrorEnums
     * @param data
     * @return
     */
    public static <T> CommonResponse<T> failure(BusinessErrorEnums businessErrorEnums, T data) {
        return create(businessErrorEnums.getErrCode(), businessErrorEnums.getErrMsg(), data);
    }


    /**
     * 自创建异常
     *
     * @param code 消息code
     * @param msg  消息
     * @param data 数据
     * @return
     */
    public static <T> CommonResponse<T> create(Integer code, String msg, T data) {
        return new CommonResponse<>(code, msg, data);
    }


}
