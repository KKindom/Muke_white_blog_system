package com.zsc.blog.Utils.responData;

import lombok.Data;

/**
 * @program: demo
 * @description: 响应体类
 * @author: 某网络大神
 * @create: 2020-07-24 17:30
 **/
@Data
public class ResponseData<T> extends BaseResponse {

    /**
     * 数据
     */
    private T data;
    private Integer num;

    public ResponseData(CodeEnum code) {
        super(code);
    }

    public ResponseData(CodeEnum code, T data) {
        super(code);
        this.data = data;
    }
    public ResponseData(CodeEnum code, T data,int num) {
        super(code);
        this.data = data;
        this.num=num;
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
    public static <T> ResponseData<T> out(CodeEnum code, T data,int num) {
        return new ResponseData<>(code, data,num);
    }
}