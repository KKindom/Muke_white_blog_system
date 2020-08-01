package com.zsc.blog.web.AdminController;

import com.alibaba.fastjson.JSONObject;
import com.zsc.blog.Utils.*;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.config.PassToken;
import com.zsc.blog.config.UserLoginToken;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITCommentService;
import com.zsc.blog.service.ITUserService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    ITCommentService itCommentService;
    @Autowired
    MailUtils mailUtils;
    @Resource
    RedisUtil redisUtil;
    @Autowired
    Encrypt_DecryptUtil encrypt_decryptUtil;
    @Autowired
    FileUploadUtils fileUploadUtils;
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @ResponseBody
    @PostMapping("/test")
    private Object  index( @RequestParam String username)
    {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
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
        TUser userForBase=itUserService.selectByusername(username);
        System.out.println(userForBase);
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
        list.addAll(itArticleService.selectList(null));
        list.add(itCommentService.selectList(null));
        list.add("asdasadada");


        //加密密码
//        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
//        textEncryptor.setPassword("password");
//        String newPassword = textEncryptor.encrypt("123456");
//        System.out.println(newPassword);
//        TUser tUser=new TUser();
//        tUser.setUsername("mff");
//        tUser.setEmail("1184045779@qq.com");
//        tUser.setPermisson("AdminController");
//        tUser.setPassword(newPassword);
//        itUserService.insert_user(tUser);
//        System.out.println(tUser);
        return ResponseData.out(CodeEnum.SUCCESS, list);
    }
    //测试发送邮件
    @ResponseBody
    @GetMapping("/test")
    private String  login()
    {   TUser tUser;
        //redisUtil.del("马飞凡");
        //测试缓存功能
//        if(redisUtil.get("admin")==null) {
//            tUser = itUserService.selectByusername("admin");
//            System.out.println("已添加缓存");
//            redisUtil.set("admin",tUser);
//        }
//        else
//        {
//            tUser=(TUser) redisUtil.get("admin");
//            System.out.println("现在从换成你数据");
//        }
//        return tUser.getNickname();
        //测试加密解密工具类
        System.out.println(encrypt_decryptUtil.Encrypt("12345"));
        System.out.println(encrypt_decryptUtil.Decrypt("DtlH4obuiLYz6U9ALRbofA=="));
        //mailUtils.sendSimpleEmail("1184045779@qq.com","个人博客系统密码找回","您的密码已重置为：123456");
        return "test";
    }

    //测试发送邮件
    @PassToken
    @ResponseBody
    @PostMapping("/test3333")
    private String  aaa(@RequestParam(name = "file", required = false) MultipartFile file)
    {
        try {
            fileUploadUtils.upload(file,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "test";
    }



        //@UserLoginToken
    @ResponseBody
    @GetMapping("/fu")
    public String say(HttpServletRequest httpServletRequest) {
        System.out.println("Hello springboot");
        return  "111";
        //System.out.println( httpServletRequest.getServletContext().getAttribute("permission"));
        //return itArticleService.tt(0,4,1);
    }
}
