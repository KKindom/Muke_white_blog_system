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
    FAILURE_error_permisson(500,"对不起，没有管理员权限！"),
    /* 验证码错误*/
    FAILURE_error_vcode(500,"对不起，验证码错误！"),
    /*注册成功*/
    SUCCESS_registeruser(500,"恭喜你注册成功!"),
    /*发送验证码成功*/
    SUCCESS_sendvcode(500,"验证码发送成功！请检查邮箱 若没收到请检查邮箱是否设置相关属性！"),
    /*修改头像成功码*/
    SUCCESS_updataphoto(500,"修改头像成功！"),
    /*修改头像失败码*/
    FAILURE_updataphoto(500,"修改头像失败！"),
    /*新增评论成功码*/
    SUCCESS_addcomment(200,"添加评论成功！");

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
