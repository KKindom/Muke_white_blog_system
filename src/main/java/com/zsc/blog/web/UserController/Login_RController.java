package com.zsc.blog.web.UserController;

import com.alibaba.fastjson.JSON;
import com.zsc.blog.Utils.Encrypt_DecryptUtil;
import com.zsc.blog.Utils.MailUtils;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.Utils.userUtil;
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

                String token = userUtil.getToken(back_user);
                data.put("token", token);
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
            TUser newuser = new TUser();
            newuser.setUsername(R_username);
            newuser.setNickname(Registerdata.get("nickname"));
            newuser.setEmail(Registerdata.get("mail"));
            newuser.setPassword(encrypt_decryptUtil.Encrypt(Registerdata.get("password")));
            newuser.setProfilephoto("https://timgsa.baidu" +
                    ".com/timg?image&quality=80&size=b9999_10000&sec=1596123710448&di=cc0bc85abd66d6562a4b9dbcdeb0da73&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F6e8f56b2543cce8bffa35b22d03684fae76a1b2c56c32-COdswi_fw658");
            newuser.setPermisson("cilent");
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