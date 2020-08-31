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
    FAILURE_error_permisson(401,"对不起，没有管理员权限！"),
    /* 验证码错误*/
    FAILURE_error_vcode(500,"对不起，验证码错误！"),
    /*注册成功*/
    SUCCESS_registeruser(200,"恭喜你注册成功!"),
    /*发送验证码成功*/
    SUCCESS_sendvcode(200,"验证码发送成功！请检查邮箱 若没收到请检查邮箱是否设置相关属性！"),
    /*修改头像成功码*/
    SUCCESS_updataphoto(200,"修改头像成功！"),
    /*提交申请成功*/
    SUCCESS_apply_author(200,"提交申请创作者成功！"),
    /*修改头像失败码*/
    FAILURE_updataphoto(500,"修改头像失败！"),
    /*新增评论成功码*/
    SUCCESS_addcomment(200,"添加评论成功！"),
    /* 修改密码失败*/
    FAILURE_updatavcode(500,"对不起验证码验证失败，请检查是否输入正确4位验证码！"),
    /* 修改密码失败*/
    FAILURE_updatapsw(500,"对不起与原密码不符合，请检查是否输入正确的原密码！"),
    /* 提交申请重复*/
    FAILURE_apply_author(500,"对不起，请不要重复申请，每次申请有效期为1天，请等待管理员的审批！"),
    /* 提交申请重复*/
    FAILURE_no_permisson(500,"对不起，你的账号被临时封禁了，请联系管理员的解封！管理员联系方式：110 邮箱：1184045779@qq.com");
    /* 内容包括敏感词*/
    FAILURE_Sensitive(500, "文章内容中包含敏感词，请修改后重新发布！");

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
