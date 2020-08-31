package com.zsc.blog.web.AdminController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.Utils.userUtil;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminLoginController {

    @Autowired
    ITUserService itUserService;

    @ResponseBody
    @PostMapping("admin/login")
    public ResponseData<Object> login(@RequestBody Map<String, String> userdata) {
        System.out.println(userdata);
        String username = userdata.get("username");
        String password = userdata.get("password");
        TUser back_user = itUserService.selectByusername(username);

        if (back_user != null) {
            Map<String, String>data = new HashMap<>();
            //判断权限
            if(!back_user.getPermisson().equals("admin") && !back_user.getPermisson().equals("root")) {
                //data.put("error", "Illegal account!");
                return ResponseData.out(CodeEnum.FAILURE_error_permisson, null);
            }

            String psw = back_user.getPassword();
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword("password");
            psw = textEncryptor.decrypt(psw);
            //解密数据库传来的密码
            if (psw.equals(password)) {
                userUtil userUtil = new userUtil();
                String token = userUtil.getToken(back_user);
                data.put("token", token);
                data.put("nickName", back_user.getNickname());

                return ResponseData.out(CodeEnum.SUCCESS, data);
            }
            return ResponseData.out(CodeEnum.FAILURE_error_password, null);
        }
        return ResponseData.out(CodeEnum.FAILURE_no_username, null);
    }

    @ResponseBody
    @PostMapping("admin/logout")
    public ResponseData<Object> logout(@RequestHeader("token") String token) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date localDate = new Date();
        long a = 0;
        try {
            Date startDate = sdf.parse("2020-08-27");
            a = (localDate.getTime() - startDate.getTime()) / (60*60*24*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ResponseData.out(CodeEnum.SUCCESS, a);
    }
}
