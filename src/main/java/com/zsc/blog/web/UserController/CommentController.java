package com.zsc.blog.web.UserController;

import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TComment;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITCommentService;
import com.zsc.blog.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @program: blog_system_f
 * @description: 用于评论
 * @author: Mr.Wang
 * @create: 2020-08-17 22:58
 **/
@Controller
@RequestMapping("/user")
public class CommentController
{
    @Autowired
    ITCommentService itCommentService;
    @Autowired
    ITUserService itUserService;
    @Autowired
    ITArticleService itArticleService;
    //查看评论列表
    @ResponseBody
    @RequestMapping(value = "/comment/getcomlist",method = RequestMethod.POST)
    public ResponseData<Object> sel_CollectList(@RequestBody Map<String,String> userdata)
    {
        //用户名
        String username=userdata.get("username");
        TUser tUser=itUserService.selectByusername(username);
        int a_id=tUser.getId();
        if(tUser!=null)
        {
          List<TComment> tCommentList=itCommentService.Selectbyuid(username);
            System.out.println("查找评论成功！");
            return ResponseData.out(CodeEnum.SUCCESS, tCommentList);
        }
        return ResponseData.out(CodeEnum.FAILURE, null);
    }
    //删除评论
    @ResponseBody
    @RequestMapping(value = "/comment/delcom",method = RequestMethod.POST)
    public ResponseData<Object> Ins_CollectList(@RequestBody Map<String,String> userdata)
    {
        //用户名
        String username=userdata.get("username");
        TUser tUser=itUserService.selectByusername(username);
        //删除文章的文章id
        String a_id=userdata.get("a_id");
        int aid=Integer.valueOf(a_id);
        if(tUser!=null)
        {
            itCommentService.Delcomment(aid,username);
            System.out.println("删除评论成功！");
            return ResponseData.out(CodeEnum.SUCCESS, null);
        }
        return ResponseData.out(CodeEnum.FAILURE, null);
    }

}
