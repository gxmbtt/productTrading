package com.example.producttrading.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author guoxin
 * @date 2026年04月02日 16:57
 */
@ControllerAdvice(basePackages = "com.example.producttrading.controller")
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        logger.error("系统异常",e);
        return Result.error("系统异常");
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e) {
        logger.error("自定义异常",e);
        return Result.error(e.getCode(),e.getMessage());
    }
}
