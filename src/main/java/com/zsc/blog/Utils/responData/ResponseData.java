package com.zsc.blog.Utils.responData;

import lombok.Data;

/**
 * @program: demo
 * @description: mff
 * @author: Mr.Wang
 * @create: 2020-07-24 17:30
 **/
@Data
public class ResponseData<T> extends BaseResponse {

    /**
     * 数据
     */
    private T data;
    private Integer num;

    private ResponseData(CodeEnum code) {
        super(code);
    }

    public ResponseData(CodeEnum code, T data) {
        super(code);
        this.data = data;
    }

    /**
     * 对外开放基础响应体已供调用，可用于增、删、改接口操作
     */
    public static BaseResponse out(CodeEnum code) {
        return new BaseResponse(code);
    }

    /**
     * 对外开放数据响应体已供调用，可用于查询数据实用，引用了范型设计，支持各种数据类型
     */
    public static <T> ResponseData<T> out(CodeEnum code, T data) {
        return new ResponseData<>(code, data);
    }
    // 省略get/set方法
}