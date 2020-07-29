package com.zsc.blog.Utils;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;

/**
 * @program: blog_system_f
 * @description: 加密解密工具类
 * @author: Mr.Wang
 * @create: 2020-07-28 19:53
 **/
@Component
public class Encrypt_DecryptUtil
{

    public String Encrypt(String password)
    {
        BasicTextEncryptor textEncryptor1=new BasicTextEncryptor();
        textEncryptor1.setPassword("password");
        System.out.println("密码已加密");
        String psw=textEncryptor1.encrypt(password);
      return   psw;
    }

    public String Decrypt(String password)
    {
        BasicTextEncryptor textEncryptor2=new BasicTextEncryptor();
        textEncryptor2.setPassword("password");
        System.out.println("密码已解密");
    String    psw=textEncryptor2.decrypt(password);
        return   psw;
    }
}
