package com.auzcean.macolabackend.common;

import com.auzcean.macolabackend.exception.ErrorCode;

/**
 * function: response tools
 */
public class ResultUtils {

    /**
     * 成功
     * @param data  数据
     * @return      返回
     * @param <T>   数据类型
     */
    public static <T>BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     * @param code      自定义错误码
     * @param message   自定义错误信息
     * @return          返回
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     * @param errorCode 错误码
     * @return          响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode 错误码
     * @param message   自定义错误信息
     * @return          返回
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }

}
