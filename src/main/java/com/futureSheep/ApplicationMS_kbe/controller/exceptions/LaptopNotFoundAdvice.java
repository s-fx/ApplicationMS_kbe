package com.futureSheep.ApplicationMS_kbe.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LaptopNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(LaptopNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(LaptopNotFoundException ex) {
        return ex.getMessage();
    }
}
