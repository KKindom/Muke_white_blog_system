package com.zsc.blog.Utils.responData;

import lombok.Data;

/**
 * 统一返回状态码
 */
public enum CodeEnum {
    /* 成功状态码 */
    SUCCESS(200, "成功"),
    /* 失败状态码 */
    FAILURE(500, "失败");


    private Integer code;
    private String  msg;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this. msg = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this. msg;
    }
    public Integer getCode()
    {
        return this.code;
    }
    public void setCode(Integer Code)
    {
         this.code=Code;
    }
    public void setmsg(String Msg)
    {
        this.msg=Msg;
    }

    public String getMsg() {
       return this.msg;
    }
}
