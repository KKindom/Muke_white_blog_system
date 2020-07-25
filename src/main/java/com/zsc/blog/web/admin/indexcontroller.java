package com.zsc.blog.web.admin;

import com.alibaba.fastjson.JSONObject;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.Utils.userUtil;
import com.zsc.blog.config.UserLoginToken;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITUserService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: demo
 * @description: mff
 * @author: Mr.Wang
 * @create: 2020-07-24 15:00
 **/
@Controller
public class indexcontroller {
    @Autowired
    ITArticleService itArticleService;
    @Autowired
    ITUserService itUserService;
    @ResponseBody
    @PostMapping("/tet")
    private Object  index( @RequestParam String username)
    {BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
textEncryptor.setPassword("password");
String newPassword = textEncryptor.encrypt("123456");
System.out.println(newPassword);
// 解密
BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
textEncryptor2.setPassword("password");
String oldPassword = textEncryptor2.decrypt(newPassword);
System.out.println(oldPassword);
System.out.println("--------------------------");

        JSONObject jsonObject=new JSONObject();
        userUtil userUtil=new userUtil();
        TUser userForBase=itUserService.selectById(username);
        System.out.println(username);
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在");
            return jsonObject;
        }else {

                String token = userUtil.getToken(userForBase);
                jsonObject.put("accessToken", token);
                jsonObject.put("user", userForBase);
                System.out.println(jsonObject);
                return jsonObject;

        }
    }
    @ResponseBody
    //传递多个不同类型数据
    @GetMapping("/testdata")
    public ResponseData<List<Object>> userInfo() {
        List<Object> list = new ArrayList<>();
        list.addAll(itUserService.selectList(null));
        list.add("asdasadada");
        return ResponseData.out(CodeEnum.SUCCESS, list);
    }
    @RequestMapping("/tt")
    private String  login()
    {
        return "test";

    }

    @UserLoginToken
    @ResponseBody
    @GetMapping("/fu")
    public String say(HttpServletRequest httpServletRequest) {
        System.out.println("Hello springboot");
        return "hello,this is a springboot demo！~";
    }
}
