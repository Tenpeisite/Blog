package com.zhj.handler.exception;

import com.zhj.domin.Result;
import com.zhj.enums.AppHttpCodeEnum;
import com.zhj.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/21 16:20
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public Result systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        //从异常对象中获取提示信息封装返回
        return Result.errorResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        //从异常对象中获取提示信息封装返回
        return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMsg());
    }
}
