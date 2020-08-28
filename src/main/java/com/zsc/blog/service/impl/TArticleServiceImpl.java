package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TCollect;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.mapper.TCollectMapper;
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
    TCollectMapper tCollectMapper;
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
    public List<Map<String, Object>> admin_select_page(int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("page_"+num+"pageSize_"+pageSize)==null)
        {
            System.out.println("我查数据库");
            resultList=tArticleMapper.admin_selectPage(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("page_"+num+"pageSize_"+pageSize,resultList,10);
        }
        else
        {
            System.out.println("我没查数据库");
            resultList =(List<Map<String, Object>>)redisUtil.get("pageNo_"+num+"pageSize_"+pageSize);
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
    public void publish(@Autowired TArticle article) {
        article.setId(tArticleMapper.getNextId());
        tStatisticMapper.addStatistic(article);
        tArticleMapper.publishArticle(article);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", article.getId().toString());
        hashMap.put("title", article.getTitle());
        hashMap.put("content", article.getContent());
        hashMap.put("created", article.getCreated());
        //hashMap.put("modified", article.getModified().toString());
        hashMap.put("categories", article.getCategories());
        redisUtil.set("article_" + article.getId().toString(), hashMap);
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
        redisUtil.del("article_"+Integer.toString(id));
        redisUtil.removeAll("comment");
        redisUtil.removeAll("page");
        redisUtil.removeAll("Afind");
        tCollectMapper.deleteColletWithAid(id);
        tCommentMapper.deleteCommentWithAid(id);
        tArticleMapper.deleteArticleWithId(id);
        tStatisticMapper.deleteStatisticWithId(id);
    }

    @Override
    public TArticle selectArticleWithId(int id) {
        TArticle tArticle = tArticleMapper.selectArticleWithId(id);
        return tArticle;
    }

    @Override
    public void updateArticle(TArticle article) {
        redisUtil.del("article_" + Integer.toString(article.getId()));
        redisUtil.removeAll("page");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", article.getId().toString());
        hashMap.put("title", article.getTitle());
        hashMap.put("content", article.getContent());
        hashMap.put("created", article.getCreated());
        hashMap.put("modified", article.getModified().toString());
        hashMap.put("categories", article.getCategories());
        redisUtil.set("article_" + article.getId().toString(), hashMap);
        tArticleMapper.updateArticle(article);
    }

    @Override
    public int queryArticleNumber() {
        return tArticleMapper.queryCount();
    }


    @Override
    public  List<Map<String, String>> selectArticleby_key(String key) {
        List<Map<String, String>> tArticleList;
        if(redisUtil.get("Afindlike_"+key)==null)
        {
            tArticleList=tArticleMapper.select_content_withAll(key);
            redisUtil.set("Afindlike_"+key,tArticleList,180000);
            System.out.println("添加缓存成功");
        }
        else
        {
            tArticleList=( List<Map<String, String>>)redisUtil.get("Afindlike_"+key);
            System.out.println("现在从缓存拿数据");
        }
        return tArticleList;
    }

    @Override
    public  List<Map<String, String>> selecttArticleby_categories(String type) {
        List<Map<String, String>> tArticleList;
        if(redisUtil.get("Afindtype_"+type)==null)
        {
            tArticleList=tArticleMapper.selectbytype(type);
            redisUtil.set("Afindtype_"+type,tArticleList,180000);
            System.out.println("添加缓存成功");
        }
        else
        {
            tArticleList=( List<Map<String, String>>)redisUtil.get("Afindtype_"+type);
            System.out.println("现在从缓存拿数据");
        }
        return tArticleList;
    }
}
