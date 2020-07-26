package com.zsc.blog.web.client;

import com.alibaba.fastjson.JSONObject;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.Utils.userUtil;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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

    public BasicTextEncryptor textEncryptor;
    @PostMapping("/login")
    public ResponseData<List<Object>> login(@RequestParam("username") String username, @RequestParam("password") String password)
    {
        System.out.println(username+password);
        TUser back_user=itUserService.selectByname(username);
        if(back_user!=null)
        {
            String psw=back_user.getPassword();
            psw= textEncryptor.decrypt(psw);
            if (back_user.getPassword().equals(password))
            {    userUtil userUtil=new userUtil();
                JSONObject jsonObject=new JSONObject();
                String token = userUtil.getToken(back_user);
                jsonObject.put("token", token);
                jsonObject.put("user", back_user);
                List<Object> list=new ArrayList<>();
                list.add(jsonObject);
                System.out.println(list);

                return ResponseData.out(CodeEnum.SUCCESS, list);
            }
        }
        return ResponseData.out(CodeEnum.FAILURE, null);
    }
}
