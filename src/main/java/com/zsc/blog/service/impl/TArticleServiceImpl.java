package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.Page_article;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.ITArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

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
    @Resource
    RedisUtil redisUtil;

    @Override
    public Collection<?> selectList(Object o) {

        return tArticleMapper.selectList(null);
    }


    @Override
    public List<Page_article> select_page(int st, int en,int num) {
        List<Page_article> resultlist;
        if (redisUtil.get("page_"+num)==null)
        {
            System.out.println("我查数据库");
            resultlist=tArticleMapper.selectpage(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("page_"+num,resultlist,1800);
        }
        else
        {
            System.out.println("我没查数据库");
            resultlist =(List<Page_article>)redisUtil.get("page_"+num);
        }
        return resultlist;
    }

    @Override
    public List<Page_article> admin_select_page(int st, int en,int num) {
        List<Page_article> resultlist;
        if (redisUtil.get("pageNo_"+num)==null)
        {
            System.out.println("我查数据库");
            resultlist=tArticleMapper.selectnewpage(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("pageNo_"+num,resultlist);
        }
        else
        {
            System.out.println("我没查数据库");
            resultlist =(List<Page_article>)redisUtil.get("pageNo_"+num);
        }
        return resultlist;
    }

    @Override
    public int allarticle() {
        return tArticleMapper.selectCount(null);
    }

    @Override
    public List<Page_article> select_newpage(int st, int en, int num) {
        List<Page_article> resultlist;
        if (redisUtil.get("newpage_"+num)==null)
        {
            System.out.println("我查数据库");
            resultlist=tArticleMapper.selectnewpage(st, en);
            System.out.println("查询" +st+en);
            redisUtil.set("newpage_"+num,resultlist,1800);
        }
        else
        {
            System.out.println("我没查数据库");
            resultlist =(List<Page_article>)redisUtil.get("newpage_"+num);
        }
        return resultlist;
    }
}
