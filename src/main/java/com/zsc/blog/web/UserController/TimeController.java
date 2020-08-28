package com.zsc.blog.web.UserController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.entity.TUsertime;
import com.zsc.blog.mapper.TUsertimeMapper;
import com.zsc.blog.service.ITUserService;
import com.zsc.blog.service.ITUsertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @program: blog_system_f
 * @description: 用于个人浏览记录
 * @author: Mr.Wang
 * @create: 2020-08-27 15:22
 **/
@Controller
@RequestMapping("/user")
public class TimeController {
    @Autowired
    TUsertimeMapper tUsertimeMapper;
    @Autowired
    ITUsertimeService itUsertimeService;
    @Autowired
    ITUserService itUserService;
    Date date=new Date();
    java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    //测试
    @ResponseBody
    @RequestMapping(value = "/test2222333",method = RequestMethod.POST)
    public ResponseData<Map<String,String>> updata_wwUname(@RequestBody Map<String, String> userdata) {

        TUsertime tUsertime=itUsertimeService.findusertime(1,"2020-06-30");
        System.out.println(tUsertime);
        return ResponseData.out(CodeEnum.SUCCESS,null);
    }
    //更新今日时间
    @ResponseBody
    @RequestMapping(value = "/updatatime",method = RequestMethod.POST)
    public ResponseData<TUsertime> updatatime(@RequestBody Map<String, String> userdata) {
        String str = sdf.format(date);
        String s_time=userdata.get("time");
        int time=Integer.valueOf(s_time);
        String username=userdata.get("username");
        //检查是否存在新的记录没有则重新插入新的记录
        int uid=checktime(username,str);
        //找到今日记录
        TUsertime tUsertime=itUsertimeService.findusertime(uid,str);
        //更新阅读时间
        tUsertime.setTime(tUsertime.getTime()+time);
        itUsertimeService.updatatime(tUsertime);
        System.out.println(tUsertime);
        return ResponseData.out(CodeEnum.SUCCESS,tUsertime);
    }
    //查看今日情况
    @ResponseBody
    @RequestMapping(value = "/checktime",method = RequestMethod.POST)
        public ResponseData<TUsertime> checktime(@RequestBody Map<String, String> userdata) {
        String str = sdf.format(date);
        String username=userdata.get("username");
        //检查是否存在新的记录没有则重新插入新的记录
       int uid= checktime(username,str);
       TUsertime tUsertime=itUsertimeService.findusertime(uid,str);
        return ResponseData.out(CodeEnum.SUCCESS,tUsertime);
    }
    //检测是否存在今天的记录
    public int checktime(String username,String data)
    {
        TUser tUser=itUserService.selectByusername(username);
        int uid=tUser.getId();
        TUsertime tUsertime=itUsertimeService.findusertime(uid,data);

        if(tUsertime==null)
        {
            TUsertime tUsertime1=new TUsertime(uid,data);
            itUsertimeService.inserttime(tUsertime1);
            System.out.println(tUsertime1);
            System.out.println("插入新的记录成功！");
        }
        return uid;
    }
}
