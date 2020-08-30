package com.zsc.blog.web.UserController;

import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.*;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: blog_system_f
 * @description: 主要写关于前台文章相关操作
 * @author: Mr.Wang
 * @create: 2020-07-29 21:43
 **/
@Controller
@RequestMapping("/user")
public class ArticleController {
    @Autowired
    ITArticleService itArticleService;
    @Autowired
    TArticleMapper tArticleMapper;
    @Autowired
    ITCommentService itCommentService;
    @Autowired
    ITUsertimeService itUsertimeService;
    @Autowired
    ITStatisticService itStatisticService;
    @Autowired
    ITUserService itUserService;
    @Resource
    RedisUtil redisUtil;
    Date date=new Date();
    java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @ResponseBody
    @RequestMapping(value = "/article/getList",method = RequestMethod.POST)
    public ResponseData<Object> Get_ArticleList(@RequestBody Map<String,String>  pageNo)
    {   //获得总共数据条数
        int num_all=itArticleService.allArticle();
        int MAX_Page=num_all/4+1;
        int last=num_all%4;
        //获取需要第几页
        int nowpage= Integer.parseInt( pageNo.get("pageNo") );
        List<Map<String,Object>> page_articles;
        if(MAX_Page>nowpage)
        {
            page_articles= itArticleService.select_page(nowpage*4-4,4,nowpage);
        }
        else
        {
            page_articles= itArticleService.select_page(nowpage*4-4,last,nowpage);
        }
        if(page_articles.size()==0)
        {
            return ResponseData.out(CodeEnum.FAILURE, null);
        }

        return ResponseData.out(CodeEnum.SUCCESS, page_articles,num_all);
    }
    @ResponseBody
    @RequestMapping(value = "/article/getnewList",method = RequestMethod.POST)
    public ResponseData<Object> Get_newArticleList(@RequestBody Map<String,String>  pageNo) {   //获得总共数据条数
        int num_all = itArticleService.allArticle();
        int MAX_Page = num_all / 4 + 1;
        int last = num_all % 4;
        //获取需要第几页
        int nowpage = Integer.parseInt(pageNo.get("pageNo"));
        List<Map<String, Object>> page_articles;
        if (MAX_Page > nowpage) {
            page_articles = itArticleService.select_newpage(nowpage * 4 - 4, 4, nowpage);
        } else {
            page_articles = itArticleService.select_newpage(nowpage * 4 - 4, last, nowpage);
        }
        //System.out.println(page_articles);
        if (page_articles.size() == 0) {
            return ResponseData.out(CodeEnum.FAILURE, null);
        }
        return ResponseData.out(CodeEnum.SUCCESS, page_articles, num_all);
    }
    //点击显示文字内容以及评论
    @ResponseBody
    @RequestMapping(value = "/article/showarticle",method = RequestMethod.POST)
    public ResponseData<Object> Show_article(@RequestBody Map<String,String>  requestdata)
    {
        String id=requestdata.get("id");
        String username=requestdata.get("username");
        String str = sdf.format(date);
        TArticle tArticle=tArticleMapper.selectArticleWithId(Integer.parseInt(id) );
        List<Map<String ,String >> tCommentList=itCommentService.SelectByArticle_id(Integer.parseInt(id));
        Map<String,Object> map=new HashMap<>();
        map.put("article",tArticle);
        map.put("commentlist",tCommentList);

        if(username!=null)
        {
            //更新用户记录
            TUsertime tUsertime = checktime(username, str);
            tUsertime.setNum(tUsertime.getNum() + 1);
            itUsertimeService.updatanum(tUsertime);
        }
        //更新文章统计数
        TStatistic statistic=itStatisticService.findbyid(Integer.parseInt(id));
        statistic.setHits(statistic.getHits()+1);
        itStatisticService.updatahits(statistic);

        return ResponseData.out(CodeEnum.SUCCESS,map);
    }
    //添加评论
    @ResponseBody
    @RequestMapping(value = "/article/addcomment",method = RequestMethod.POST)
    public ResponseData<Object> Add_comment(@RequestBody Map<String,String>  requestdata)
    {
        String article_id=requestdata.get("article_id");
        String content=requestdata.get("content");
        String author=requestdata.get("author");
        String status=requestdata.get("status");
        //string 转 时间戳
        Timestamp ldt =new Timestamp(new Date().getTime());
        //初始化赋值评论
        TComment newcomment=new TComment();
        newcomment.setArticleId(Integer.valueOf(article_id));
        newcomment.setAuthor(author);
        newcomment.setContent(content);
        newcomment.setCreated(ldt);
        newcomment.setStatus(status);
        itCommentService.insertcomment(newcomment);
        //更新文章统计评论是
        TStatistic statistic=itStatisticService.findbyid(Integer.valueOf(article_id));
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        itStatisticService.updatacom(statistic);

        return ResponseData.out(CodeEnum.SUCCESS_addcomment,null);
    }


    //模糊搜索
    @ResponseBody
    @RequestMapping(value = "/article/checkkey",method = RequestMethod.POST)
    public ResponseData<Object> checkkeylist(@RequestBody Map<String,String>  requestdata) {

         String key= requestdata.get("key");
          List<Map<String, String>> mapList=itArticleService.selectArticleby_key(key);
          return ResponseData.out(CodeEnum.SUCCESS,mapList);
    }
    //按照分类查询
    @ResponseBody
    @RequestMapping(value = "/article/checktype",method = RequestMethod.POST)
    public ResponseData<Object> checktypelist(@RequestBody Map<String,String>  requestdata) {

        String type= requestdata.get("type");
        List<Map<String, String>> mapList=itArticleService.selecttArticleby_categories(type);
        return ResponseData.out(CodeEnum.SUCCESS,mapList);
    }

    //获得用户名下的文章
    @ResponseBody
    @RequestMapping(value = "/article/getListbyauthor",method = RequestMethod.POST)
    public ResponseData<Object> getListbyauthor(@RequestBody Map<String,String>  requestdata) {

        String author= requestdata.get("author");
        List<Map<String, String>> mapList=itArticleService.selectArticleby_author(author);
        System.out.println(mapList);
        return ResponseData.out(CodeEnum.SUCCESS,mapList);
    }








    //检测是否存在今天的记录
    public TUsertime checktime(String username,String data)
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
            return tUsertime1;
        }
        return tUsertime;
    }

}
