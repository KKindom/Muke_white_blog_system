package com.zsc.blog.web.UserController;

import com.alibaba.fastjson.JSON;
import com.zsc.blog.Utils.*;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @program: demo
 * @description: mff
 * @author: Mr.Wang
 * @create: 2020-07-25 17:46
 **/
@Controller
@RequestMapping("/user")
public class Login_RController {
    @Autowired
    ITUserService itUserService;
    @Autowired
    MailUtils mailUtils;
    @Resource
    RedisUtil redisUtil;
    @Resource
    Photo_list photo_list;
    @Autowired
    Encrypt_DecryptUtil encrypt_decryptUtil;
    //验证码
    String  Identifying_code;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseData<Map<String, String>> login(@RequestBody Map<String, String> userdata) {
        System.out.println(userdata);
        String username = userdata.get("username");
        String password = userdata.get("password");
        TUser back_user = itUserService.selectByusername(username);
        if (back_user != null) {
            //解密数据库传来的密码
            String psw = back_user.getPassword();
            psw = encrypt_decryptUtil.Decrypt(psw);
            System.out.println("解密后密码" + psw);
            if (psw.equals(password)) {
                userUtil userUtil = new userUtil();
                Map<String, String> data = new HashMap<>();
                if(redisUtil.get("Apply_Author_"+username)==null)
                {
                    data.put("Apply","false");
                }
                else
                {
                    data.put("Apply","true");
                }
                String token = userUtil.getToken(back_user);
                data.put("token", token);
                data.put("permisson",back_user.getPermisson());
                data.put("nickName", back_user.getNickname());
                data.put("emil",back_user.getEmail());
                return ResponseData.out(CodeEnum.SUCCESS, data);
            }
            return ResponseData.out(CodeEnum.FAILURE_error_password, null);
        }
        return ResponseData.out(CodeEnum.FAILURE_no_username, null);
    }

    @ResponseBody
    @PostMapping("/logout")
    public ResponseData<Map<String, String>> logout(@RequestHeader("token") String token) {
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }

    @ResponseBody
    @PostMapping("/Register")
    public ResponseData<Object> Register(@RequestBody Map<String, String> Registerdata) {
        String R_username = Registerdata.get("username");
        System.out.println(Identifying_code);
        //判定验证码是否正确
        if(!Registerdata.get("code").equals(Identifying_code))
        {
            return ResponseData.out(CodeEnum.FAILURE_error_vcode, null);

        }
        //判定缓存中以及数据库中是否存在用户名，有则返回重复用户名错误
       else if (redisUtil.get(R_username) != null || itUserService.selectByusername(R_username) != null) {
            return ResponseData.out(CodeEnum.FAILURE_error_username, null);
        }
       //均满足后插入数据库
       else
        {
            //随机获取头像
            String photo=photo_list.find_photo();
            TUser newuser = new TUser();
            newuser.setUsername(R_username);
            newuser.setNickname(Registerdata.get("nickname"));
            newuser.setEmail(Registerdata.get("mail"));
            newuser.setPassword(encrypt_decryptUtil.Encrypt(Registerdata.get("password")));
            newuser.setProfilephoto(photo);
            newuser.setPermisson("client");
            itUserService.insert_user(newuser);
            return ResponseData.out(CodeEnum.SUCCESS_registeruser, newuser);
        }

    }
    //注册时获取验证码
    @ResponseBody
    @PostMapping("/Register_vcode")
    public ResponseData<Map<String, Integer>> getvcode(@RequestBody Map<String, String> Email)
    {
        //设置需要发送邮箱
        String email=Email.get("mail");
        //设置随机数
        int vcode=(int)((Math.random()*9+1)*1000);
        Identifying_code=vcode+"";
        System.out.println(Identifying_code);
        //发送邮件
        mailUtils.sendSimpleEmail(email,vcode);
        //返回验证码
        Map<String ,Integer> codemap=new HashMap<>();
        codemap.put("code",vcode);
        return ResponseData.out(CodeEnum.SUCCESS_sendvcode,codemap);
    }
}