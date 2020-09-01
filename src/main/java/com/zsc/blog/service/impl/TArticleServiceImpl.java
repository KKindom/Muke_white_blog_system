package com.zsc.blog.service.impl;

import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.mapper.TCollectMapper;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.mapper.TStatisticMapper;
import com.zsc.blog.service.ITArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
            redisUtil.set("page_"+num,resultlist,5);
        }
        else
        {
            System.out.println("page我没查数据库");
            resultlist =(List<Map<String, Object>>)redisUtil.get("page_"+num);
        }
        return resultlist;
    }

    @Override
    public List<Map<String, Object>> adminSelectPage(int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("adminpage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tArticleMapper.adminSelectPage(st, en);
            redisUtil.set("adminpage_"+num+"pageSize_"+pageSize,resultList,10);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("adminpage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> adminSelectPage(int rootId, int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("root" + rootId + "page_"+num+"pageSize_"+pageSize)==null) {
            resultList=tArticleMapper.adminSelectPageByRoot(rootId, st, en);
            redisUtil.set("root" + rootId + "page_"+num+"pageSize_"+pageSize,resultList,10);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("root" + rootId + "page_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public int allArticle() {
        return tArticleMapper.selectCount(null);
    }

    @Override
    public int allArticle(int rootId) {
        return tArticleMapper.queryCount(rootId);
    }

    @Override
    public List<Map<String, Object>> select_newpage(int st, int en, int num) {
        List<Map<String, Object>> resultlist;
        if (redisUtil.get("newpage_"+num)==null)
        {
            System.out.println("new我查数据库");
            resultlist=tArticleMapper.selectnewpagetest(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("newpage_"+num,resultlist,5);
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
        tArticleMapper.publishArticle(article);
        tStatisticMapper.addStatistic(article);
        redisUtil.removeAll("page");
        /*HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", article.getId().toString());
        hashMap.put("title", article.getTitle());
        hashMap.put("content", article.getContent());
        hashMap.put("created", article.getCreated());
        //hashMap.put("modified", article.getModified().toString());
        hashMap.put("categories", article.getCategories());*/
        redisUtil.set("article_" + article.getId().toString(), article);
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
        //删除作者文章缓存
        TArticle tArticle=tArticleMapper.selectById(id);
        String author=tArticle.getAuthor();
        redisUtil.del("Articlelist_"+author);


        tCollectMapper.deleteColletWithAid(id);
        tCommentMapper.deleteCommentWithAid(id);
        tArticleMapper.deleteArticleWithId(id);
        tStatisticMapper.deleteStatisticWithId(id);
    }

    @Override
    public TArticle selectArticleWithId(int id) {
        TArticle tArticle;
        if(redisUtil.get("article_"+id)==null) {
            tArticle= tArticleMapper.selectArticleWithId(id);
            redisUtil.set("article_"+id,tArticle);
        }
        else {
            tArticle=(TArticle)redisUtil.get("article_"+id);
        }
        return tArticle;
    }

    @Override
    public void updateArticle(TArticle article) {
        redisUtil.del("article_" + Integer.toString(article.getId()));
        redisUtil.removeAll("page");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("id", article.getId().toString());
//        hashMap.put("title", article.getTitle());
//        hashMap.put("content", article.getContent());
//        hashMap.put("created", article.getCreated());
//        hashMap.put("modified", article.getModified().toString());
//        hashMap.put("categories", article.getCategories());
        redisUtil.set("article_" + article.getId().toString(), article);
        tArticleMapper.updateArticle(article);
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

    @Override
    public List<Map<String,String>> selectArticleby_author(String author) {
        List<Map<String,String>>  tArticleList;
        if(redisUtil.get("Articlelist_"+author)==null)
        {
            tArticleList=tArticleMapper.select_list_withAuthor(author);
            redisUtil.set("Articlelist_"+author,tArticleList,180000);
        }
        else
        {
            tArticleList=(List<Map<String,String>>)redisUtil.get("Articlelist_"+author);
        }

        return tArticleList;
    }
}
