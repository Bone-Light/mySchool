package com.example.controller.exception;

import com.example.entity.RestBean;
import jakarta.validation.ConstraintViolationException;
import jakarta.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationController {

    @ExceptionHandler(ConstraintViolationException.class) // 修改为捕获 ConstraintViolationException
    public RestBean<Void> validateException(ConstraintViolationException ex) {
        log.warn("Resolve [{} : {}]", ex.getClass().getName(), ex.getMessage());
        return RestBean.failure(400, "请求参数有误");
    }

}