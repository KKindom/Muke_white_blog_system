package com.zsc.blog.web.UserController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.Utils.userUtil;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @PostMapping("/login")
    public ResponseData<Map<String,String>> login(@RequestParam("username") String username, @RequestParam("password") String password)
    {
        TUser back_user=itUserService.selectByusername(username);
        if(back_user!=null)
        {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            String psw=back_user.getPassword();
            textEncryptor.setPassword("password");
            //解密数据库传来的密码
            psw= textEncryptor.decrypt(psw);
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
//    @ResponseBody
//    @PostMapping("/logout")
//    public ResponseData<Map<String,String>> logout(@RequestHeader("token") String token) {
//        return ResponseData.out(CodeEnum.SUCCESS, null);
//    }
}
