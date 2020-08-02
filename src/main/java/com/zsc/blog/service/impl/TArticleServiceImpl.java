package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.mapper.TStatisticMapper;
import com.zsc.blog.service.ITArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
@Service
public class TArticleServiceImpl extends ServiceImpl<TArticleMapper, TArticle> implements ITArticleService {
    @Autowired
    TArticleMapper tArticleMapper;
    @Autowired
    TStatisticMapper tStatisticMapper;
    @Autowired
    TCommentMapper tCommentMapper;
    @Resource
    RedisUtil redisUtil;

    @Override
    public Collection<?> selectList(Object o) {

        return tArticleMapper.selectList(null);
    }


    @Override
    public List<Map<String, Object>> select_page(int st, int en,int num) {
        List<Map<String, Object>> resultlist;
        if (redisUtil.get("page_"+num)==null)
        {
            System.out.println("page我查数据库");
            resultlist=tArticleMapper.selectpagetest(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("page_"+num,resultlist,1800);
        }
        else
        {
            System.out.println("page我没查数据库");
            resultlist =(List<Map<String, Object>>)redisUtil.get("page_"+num);
        }
        return resultlist;
    }

    @Override
    public List<Map<String, Object>> admin_select_page(int st, int en,int num) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("pageNo_"+num)==null)
        {
            System.out.println("我查数据库");
            resultList=tArticleMapper.admin_selectPage(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("pageNo_"+num,resultList);
        }
        else
        {
            System.out.println("我没查数据库");
            resultList =(List<Map<String, Object>>)redisUtil.get("pageNo_"+num);
        }
        return resultList;
    }

    @Override
    public int allarticle() {
        return tArticleMapper.selectCount(null);
    }

    @Override
    public List<Map<String, Object>> select_newpage(int st, int en, int num) {
        List<Map<String, Object>> resultlist;
        if (redisUtil.get("newpage_"+num)==null)
        {
            System.out.println("new我查数据库");
            resultlist=tArticleMapper.selectnewpagetest(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("newpage_"+num,resultlist,1800);
        }
        else
        {
            System.out.println("new我没查数据库");
            resultlist =(List<Map<String,Object>>)redisUtil.get("newpage_"+num);
        }
        return resultlist;
    }

    @Override
    public void publish(TArticle article) {
        tArticleMapper.publishArticle(article);
        tStatisticMapper.addStatistic(article);
    }

    @Override
    public void deleteArticleWithId(int id) {
        /*删除文章封面文件 可选
        TArticle article = tArticleMapper.selectArticleWithId(id);
        String filepath = article.getThumbnail();
        File file = new File(filepath);
        if(file.exists()) {
            file.delete();
        }*/
        tCommentMapper.deleteCommentWithId(id);
        tArticleMapper.deleteArticleWithId(id);
        tStatisticMapper.deleteStatisticWithId(id);
    }
}
