package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TCollect;
import com.zsc.blog.entity.TComment;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.mapper.TCollectMapper;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.mapper.TUserMapper;
import com.zsc.blog.service.ITCollectService;
import com.zsc.blog.service.ITCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: blog_system_f
 * @description: 用户收藏表
 * @author: Mr.Wang
 * @create: 2020-08-13 22:12
 **/
@Service
public class TCollectServiceImpl extends ServiceImpl<TCollectMapper, TCollect> implements ITCollectService {
   @Autowired
   TCollectMapper tCollectMapper;
   @Autowired
    TArticleMapper tArticleMapper;
    public TCollectServiceImpl() {
        super();
    }

    @Override
    public void Delete(int a_id,int u_id) {
        tCollectMapper.deletebya_id(a_id,u_id);
        System.out.println("删除收藏文章id为："+a_id);
    }

    @Override
    public void Insert(int a_id,TUser user) {
        TArticle article=tArticleMapper.selectArticleWithId(a_id);
        TCollect tCollect=new TCollect();
        tCollect.setAId(article.getId());
        tCollect.setPhoto(article.getThumbnail());
        tCollect.setTitle(article.getTitle());
        tCollect.setUsername(user.getUsername());
        tCollect.setUId(user.getId());
        tCollectMapper.insert(tCollect);
    }

    @Override
    public List<TCollect> Select(int uid) {
        List<TCollect> tCollectList=tCollectMapper.selectList(new QueryWrapper<TCollect>().eq("u_id",uid));
        return tCollectList;
    }
}
