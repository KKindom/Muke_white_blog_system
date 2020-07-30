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

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminLogin {
    @Autowired
    ITUserService itUserService;

    @ResponseBody
    @PostMapping("admin/login")
    public ResponseData<Map<String, String>> login(@RequestBody Map<String, String> userdata) {
        System.out.println(userdata);
        String username = userdata.get("username");
        String password = userdata.get("password");
        TUser back_user = itUserService.selectByusername(username);

        if (back_user != null) {
            Map<String, String>data = new HashMap<>();
            //判断权限
            if(!back_user.getPermisson().equals("admin")) {
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
}
