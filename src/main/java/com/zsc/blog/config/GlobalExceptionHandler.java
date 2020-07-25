package com.zsc.blog.config;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: demo
 * @description: mff
 * @author: Mr.Wang
 * @create: 2020-07-25 07:54
 **/
@ControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(value = MyException.class)

    @ResponseBody

    public ResponseData<String> jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {

        ResponseData<String> r = new ResponseData<>(CodeEnum.FAILURE,"null");

        r.setMsg(e.getMessage());

        return r;

    }

}
