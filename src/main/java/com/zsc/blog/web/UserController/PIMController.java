package com.zsc.blog.web.UserController;

import com.zsc.blog.Utils.Encrypt_DecryptUtil;
import com.zsc.blog.Utils.FileUploadUtils;
import com.zsc.blog.Utils.MailUtils;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.AttachFile;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: blog_system_f
 * @description: 用于个人信息管理
 * @author: Mr.Wang
 * @create: 2020-08-01 21:29
 **/
@Controller
@RequestMapping("/user")
public class PIMController
{
    @Autowired
    ITUserService itUserService;
    @Autowired
    MailUtils mailUtils;
    @Resource
    RedisUtil redisUtil;
    @Autowired
    Encrypt_DecryptUtil encrypt_decryptUtil;
    @Autowired
    FileUploadUtils fileUploadUtils;
    //验证码
    String  Identifying_code;
    //修改头像函数
    @ResponseBody
    @RequestMapping(value = "/updata_photo",method = RequestMethod.POST)
    public ResponseData<Map<String,String>> updata_photo(@RequestParam(name = "file", required = false) MultipartFile file,
                                                         @RequestParam("username")String username)
    {
        //String username=userdata.get("username");
        TUser user=itUserService.selectByusername(username);
        try {
            AttachFile attachFile = fileUploadUtils.upload(file, 1);
            user.setProfilephoto(attachFile.getVirtual_path());
            itUserService.updata_I(user);


        } catch (IOException e) {
            return ResponseData.out(CodeEnum.FAILURE_updataphoto,null);
        }
        return ResponseData.out(CodeEnum.SUCCESS_updataphoto,null);
    }
    //修改用户名
    @ResponseBody
    @RequestMapping(value = "/updata_Uname",method = RequestMethod.POST)
    public ResponseData<Map<String,String>> updata_Uname(@RequestBody Map<String, String> userdata)
    {
        String username=userdata.get("username");
        String new_uname=userdata.get("new_uname");
        int count=itUserService.find_usercount(new_uname);
        if(count>=1)
        {
            return ResponseData.out(CodeEnum.FAILURE_error_username,null);
        }

        TUser newuser=itUserService.selectByusername(username);
        newuser.setUsername(new_uname);
        itUserService.updata_I(newuser);
        return ResponseData.out(CodeEnum.SUCCESS,null);
    }
    //修改 密码
    @ResponseBody
    @RequestMapping(value = "/updata_Pwd",method = RequestMethod.POST)
    public ResponseData<Map<String,String>> updata_Pwd(@RequestBody Map<String, String> userdata)
    {
        String username=userdata.get("username");
        String newpassword=userdata.get("newpassword");
        String password=userdata.get("password");
        System.out.println("原先密码为"+password);
        System.out.println("现在密码为:"+newpassword);
        TUser olduser=itUserService.selectByusername(username);
        //判定传来的原始密码和数据库原密码是否匹配
        if(encrypt_decryptUtil.Decrypt(olduser.getPassword()).equals(password))
        {
            //加密密码
            olduser.setPassword(encrypt_decryptUtil.Encrypt(newpassword));
            itUserService.updata_I(olduser);
        }
        else
        {
            System.out.println("密码与原密码不匹配！");
            return  ResponseData.out(CodeEnum.FAILURE,null);
        }
        return ResponseData.out(CodeEnum.SUCCESS,null);
    }

    //修改 邮箱
    @ResponseBody
    @RequestMapping(value = "/updata_Emil",method = RequestMethod.POST)
    public ResponseData<Map<String,String>> updata_Emil(@RequestBody Map<String, String> userdata)
    {
        String newemil=userdata.get("emil");
        String vcode=userdata.get("code");
        String username=userdata.get("username");
        //验证原邮箱验证码
        if (vcode.equals(Identifying_code))
        {
            TUser olduser=itUserService.selectByusername(username);
            olduser.setEmail(newemil);
            itUserService.updata_I(olduser);
            return ResponseData.out(CodeEnum.SUCCESS,null);
        }
        return ResponseData.out(CodeEnum.FAILURE,null);
    }
    //修改邮箱获取验证码
    @ResponseBody
    @PostMapping("/updata_vcode")
    public ResponseData<Map<String, Integer>> getvcode(@RequestBody Map<String, String> userdata)
    {
        //设置需要发送邮箱的用户原邮箱
        String username=userdata.get("username");
        TUser tUser=itUserService.selectByusername(username);
        String email=tUser.getEmail();
        //设置随机数
        int vcode=(int)((Math.random()*9+1)*1000);
        Identifying_code=vcode+"";
        //发送邮件
        mailUtils.sendSimpleEmail(email,vcode);
        //返回验证码
        Map<String ,Integer> codemap=new HashMap<>();
        codemap.put("code",vcode);
        return ResponseData.out(CodeEnum.SUCCESS_sendvcode,codemap);
    }
}
