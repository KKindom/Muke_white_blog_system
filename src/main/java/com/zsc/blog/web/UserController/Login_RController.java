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
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseData<Map<String,String>> login(@RequestBody Map<String,String> userdata)
    {
        System.out.println(userdata);
      String username=userdata.get("username");
       String password=userdata.get("password");
        TUser back_user=itUserService.selectByusername(username);
        if(back_user!=null)
        {
            //解密数据库传来的密码
            String psw=back_user.getPassword();
            psw=encrypt_decryptUtil.Decrypt(psw);
            System.out.println("解密后密码"+psw);
            if (psw.equals(password))
            {    userUtil userUtil=new userUtil();
                Map<String,String> data=new HashMap<>();

                String token = userUtil.getToken(back_user);
                data.put("token",token);
                data.put("nickName",back_user.getNickname());

                return ResponseData.out(CodeEnum.SUCCESS, data);
            }
            return ResponseData.out(CodeEnum.FAILURE_error_password, null);
        }
        return ResponseData.out(CodeEnum.FAILURE_no_username,null);
    }
    @ResponseBody
    @PostMapping("/logout2")
    public ResponseData<Map<String,String>> logout(@RequestHeader("token") String token) {
        return ResponseData.out(CodeEnum.SUCCESS, null);
    }
    @ResponseBody
    @PostMapping("/Register")
    public ResponseData<Map<String,String>> Register(@RequestBody Map<String,String> Registerdata)
    {
        String R_username=Registerdata.get("username");
        if(redisUtil.get(R_username)!=null|| itUserService.selectByusername(R_username)!=null)
        {
            return ResponseData.out(CodeEnum.FAILURE_error_username,null);
        }
        else
        {
            TUser newuser=new TUser();
            newuser.setUsername(R_username);
            newuser.setPassword(encrypt_decryptUtil.Encrypt(Registerdata.get("password")));

        }
        return ResponseData.out(CodeEnum.FAILURE_error_username,null);
    }
}
