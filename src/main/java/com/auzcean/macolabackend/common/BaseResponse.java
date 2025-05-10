package com.auzcean.macolabackend.common;

import com.auzcean.macolabackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * function: global response wrapper class
 * @param <T> any data
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;

    /**
     * 根据传入调用码、数据、错误码信息进行返回
     * @param code      自定义调用码
     * @param data      数据
     * @param message   自定义调用信息
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 根据传入调用码、数据进行返回
     * @param code      自定义调用码
     * @param data      数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 根据异常类进行返回
     * @param errorCode 错误码
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
