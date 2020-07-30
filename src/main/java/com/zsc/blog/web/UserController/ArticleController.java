package com.zsc.blog.web.UserController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.Utils.responData.CodeEnum;
import com.zsc.blog.Utils.responData.ResponseData;
import com.zsc.blog.entity.Page_article;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.ITArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
        List<Page_article> page_articles;
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
}
