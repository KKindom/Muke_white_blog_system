package com.zsc.blog.web.UserController;

import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TCollect;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITCollectService;
import com.zsc.blog.service.ITCommentService;
import com.zsc.blog.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: blog_system_f
 * @description: 用于收藏功能控制器
 * @author: Mr.Wang
 * @create: 2020-08-17 22:38
 **/
@Controller
@RequestMapping("/user")
public class CollcetController
{
    @Autowired
    ITArticleService itArticleService;
    @Autowired
    ITUserService itUserService;
    @Autowired
    ITCollectService itCollectService;
    @Resource
    RedisUtil redisUtil;
    //返回所有收藏
    @ResponseBody
    @RequestMapping(value = "/collect/getList",method = RequestMethod.POST)
    public ResponseData<Object> Get_CollectList(@RequestBody Map<String,String> userdata)
    {
        //用户名
        String username=userdata.get("username");
        TUser tUser=itUserService.selectByusername(username);
        int uid=tUser.getId();
        if(tUser!=null)
        {
        List<TCollect> tCollectList=    itCollectService.Select(uid);
            System.out.println("查评论成功");
            return ResponseData.out(CodeEnum.SUCCESS, tCollectList);
        }
        return ResponseData.out(CodeEnum.FAILURE, null);
        //获得总共数据条数
}
//删除收藏
    @ResponseBody
    @RequestMapping(value = "/collect/delcollect",method = RequestMethod.POST)
    public ResponseData<Object> Del_CollectList(@RequestBody Map<String,String> userdata)
    {
        //用户名
        String username =userdata.get("username");
        //删除收藏的文章id
        String a_id=userdata.get("a_id");
        int aid=Integer.valueOf(a_id);
        TUser tUser=itUserService.selectByusername(username);
        if(tUser!=null)
        {
            int uid=tUser.getId();
            itCollectService.Delete(aid,uid);
            System.out.println("删除收藏成功！");
            return ResponseData.out(CodeEnum.SUCCESS, null);
        }
        return ResponseData.out(CodeEnum.FAILURE, null);
    }
    //插入收藏
    @ResponseBody
    @RequestMapping(value = "/collect/inscollect",method = RequestMethod.POST)
    public ResponseData<Object> Ins_CollectList(@RequestBody Map<String,String> userdata)
    {
        //用户名
        String username=userdata.get("username");
        TUser tUser=itUserService.selectByusername(username);
        //删除收藏的文章id
        String a_id=userdata.get("a_id");
        int aid=Integer.valueOf(a_id);
        if(tUser!=null)
        {
            itCollectService.Insert(aid,tUser);
            System.out.println("插入收藏成功！");
            return ResponseData.out(CodeEnum.SUCCESS, null);
        }
        return ResponseData.out(CodeEnum.FAILURE, null);
    }
}
