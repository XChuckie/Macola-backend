package com.auzcean.macolabackend.exception;

import com.auzcean.macolabackend.common.BaseResponse;
import com.auzcean.macolabackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务的全局异常捕获
     * @param e 业务异常信息
     * @return  返回
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException: ", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部出现错误");
    }

}
