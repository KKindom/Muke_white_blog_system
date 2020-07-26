package com.zsc.blog;

import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: demo
 * @description: mff
 * @author: Mr.Wang
 * @create: 2020-07-26 19:12
 **/
public class adduser
{
    @Autowired
    ITUserService itUserService;
    public static void main(String[] args)
    {
        //加密密码
        TUser tUser=new TUser();
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("password");
        String newPassword = textEncryptor.encrypt("123456");
        System.out.println(newPassword);
    }
}

