package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TComment;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.service.ITCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
@Service
public class TCommentServiceImpl extends ServiceImpl<TCommentMapper, TComment> implements ITCommentService {
    @Autowired
    TCommentMapper tCommentMapper;
    @Resource
    RedisUtil redisUtil;

    @Override
    public Object selectList(Object o) {
        return tCommentMapper.selectList(null);
    }

    @Override
    public List<TComment> SelectByArticle_id(int id) {
        return tCommentMapper.selectList(new QueryWrapper<TComment>().eq("article_id", id));
    }

    @Override
    public void insertcomment(TComment tComment)
    {
        String username=tComment.getAuthor();
        if(redisUtil.get("commentlist_"+username)==null) {
            tCommentMapper.insert(tComment);
        }
        else
        {
            redisUtil.del("commentlist_"+username);
        }
        System.out.println("插入评论成功！");
    }

    @Override
    public List<Map<String,String>> Selectbyuid(String username) {
        List<Map<String,String>> CommentList;
        if (redisUtil.get(  "commentlist_"+username) == null)
        {
            CommentList = tCommentMapper.selcetcommentbyusername(username);
            redisUtil.set("commentlist_"+username,CommentList,18000);

        }
        else
        {
            CommentList=(List<Map<String,String>>) redisUtil.get("commentlist_"+username);
        }
        return CommentList;
    }

    @Override
    public void Delcomment(int aid, String username)
    {
        if(redisUtil.get("commentlist_"+username)==null)
        {
            tCommentMapper.deleteCommentWithauthor(aid, username);
        }
        else
        {
            redisUtil.del("commentlist_"+username);
        }
    }

    @Override
    public int queryCommentNumber() {
        return tCommentMapper.queryCount();
    }
}
