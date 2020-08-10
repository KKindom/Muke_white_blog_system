package com.zsc.blog.web.UserController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TComment;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.service.ITArticleService;
import com.zsc.blog.service.ITCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    RedisUtil redisUtil;
    @ResponseBody
    @RequestMapping(value = "/article/getList",method = RequestMethod.POST)
    public ResponseData<Object> Get_ArticleList(@RequestBody Map<String,String>  pageNo)
    {   //获得总共数据条数
        int num_all=itArticleService.allarticle();
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
    public ResponseData<Object> Get_newArticleList(@RequestBody Map<String,String>  pageNo)
    {   //获得总共数据条数
        int num_all=itArticleService.allarticle();
        int MAX_Page=num_all/4+1;
        int last=num_all%4;
        //获取需要第几页
        int nowpage= Integer.parseInt( pageNo.get("pageNo") );
        List<Map<String,Object>> page_articles;
        if(MAX_Page>nowpage)
        {
            page_articles= itArticleService.select_newpage(nowpage*4-4,4,nowpage);
        }
        else
        {
            page_articles= itArticleService.select_newpage(nowpage*4-4,last,nowpage);
        }
        //System.out.println(page_articles);
        if(page_articles.size()==0)
        {
            return ResponseData.out(CodeEnum.FAILURE, null);
        }
        return ResponseData.out(CodeEnum.SUCCESS, page_articles,num_all);
    }
    //点击显示文字内容以及评论
    @ResponseBody
    @RequestMapping(value = "/article/showarticle",method = RequestMethod.POST)
    public ResponseData<Object> Show_article(@RequestBody Map<String,String>  requestdata)
    {
        String id=requestdata.get("id");
        TArticle tArticle=tArticleMapper.selectArticleWithId(Integer.parseInt(id) );
        List<TComment> tCommentList=itCommentService.SelectByArticle_id(Integer.parseInt(id));
        Map<String,Object> map=new HashMap<>();
        map.put("article",tArticle);
        map.put("commentlist",tCommentList);
        return ResponseData.out(CodeEnum.SUCCESS,map);
    }
    //添加评论
    @ResponseBody
    @RequestMapping(value = "/article/addcomment",method = RequestMethod.POST)
    public ResponseData<Object> Add_comment(@RequestBody Map<String,String>  requestdata)
    {
        String article_id=requestdata.get("article_id");
        String created=requestdata.get("created");
        String content=requestdata.get("content");
        String author=requestdata.get("author");
        String status=requestdata.get("status");
        //string 转 LocalDate格式
        LocalDate ldt = LocalDate.parse(created);
        //初始化赋值评论
        TComment newcomment=new TComment();
        newcomment.setArticleId(Integer.valueOf(article_id));
        newcomment.setAuthor(author);
        newcomment.setContent(content);
        newcomment.setCreated(ldt);
        newcomment.setStatus(status);
        itCommentService.insertcomment(newcomment);
        return ResponseData.out(CodeEnum.SUCCESS_addcomment,null);
    }


}
