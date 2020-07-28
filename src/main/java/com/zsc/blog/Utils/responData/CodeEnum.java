package com.zsc.blog.Utils.responData;

import lombok.Data;
import org.omg.CORBA.PUBLIC_MEMBER;
/**
 * 统一返回状态码
 */
public enum CodeEnum {
    /* 成功状态码 */
    SUCCESS(200, "成功"),
    /* 失败状态码 */
    FAILURE(500, "失败"),
    /* 无用户名登录失败*/
    FAILURE_no_username(500,"对不起，没有查询到该用户名！请检查是否输入错误！"),
    /* 密码错误登录失败*/
    FAILURE_error_password(500,"对不起，密码错误，请检查密码是否有误！"),
    /* 存在此用户失败码*/
    FAILURE_error_username(500,"对不起，已存在此用户！"),
    /* 权限错误登陆失败*/
    FAILURE_error_permisson(500,"对不起，没有管理员权限！");

    public int code;
    public String  msg;
      CodeEnum(int code, String message) {
        this.code = code;
        this. msg = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this. msg;
    }
    public int getCode()
    {
        return this.code;
    }
    public void setCode(int Code)
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
