package com.zsc.blog.service.impl;

import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TComment;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.mapper.TStatisticMapper;
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
    @Autowired
    TStatisticMapper tStatisticMapper;
    @Resource
    RedisUtil redisUtil;

    @Override
    public Object selectList(Object o) {
        return tCommentMapper.selectList(null);
    }

    @Override
    public List<Map<String ,String >> SelectByArticle_id(int id) {
        List<Map<String ,String >> list;
        if(redisUtil.get("commentlistby_id_"+id)==null)
        {
            list=tCommentMapper.selectcomlistby_a_id(id);
            redisUtil.set("commentlistby_id_"+id,list,18000);
            System.out.println("commentlistby_id_"+id+"加入缓存成功！");
        }
        else
        {
            list=(List<Map<String ,String >>)redisUtil.get("commentlistby_id_"+id);
            System.out.println("commentlistby_id_"+id+"从缓存读出！");
        }
        return list;
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
    public List<Map<String,String>> Selectbyusername(String username) {
        List<Map<String,String>> CommentList;
        if (redisUtil.get(  "commentlist_"+username) == null)
        {
            CommentList = tCommentMapper.selcetcommentbyusername(username);
            redisUtil.set("commentlist_"+username,CommentList,180000);

        }
        else
        {
            CommentList=(List<Map<String,String>>) redisUtil.get("commentlist_"+username);
        }
        return CommentList;
    }

    @Override
    public void Delcomment(int id, String username)
    {
        if(redisUtil.get("commentlist_"+username)==null)
        {
            tCommentMapper.deleteCommentWithauthor(id, username);
        }
        else
        {
            //删除原有缓存
            redisUtil.del("commentlist_"+username);
            tCommentMapper.deleteCommentWithauthor(id, username);
        }
    }

    @Override
    public int queryCommentNumber() {
        return tCommentMapper.selectCount(null);
    }

    @Override
    public int queryCommentNumber(int rootId) {
        return tCommentMapper.querytCount(rootId);
    }

    @Override
    public int queryCommentWithAId(int id) {
        return tCommentMapper.queryCountWithAId(id);
    }

    @Override
    public int queryCommentWithAId(int rootId, int id) {
        return tCommentMapper.queryCountWithAIdByRoot(rootId, id);
    }

    @Override
    public void deleteCommentWithId(int id) {
        redisUtil.removeAll("comment");
        TComment tComment = tCommentMapper.selectById(id);
        tStatisticMapper.updateStatisticAfterDeleteComment(tComment.getArticleId());
        tCommentMapper.deleteCommentWithId(id);
    }

    @Override
    public List<Map<String, Object>> selectCommentPage(int id, int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("adminarticle_"+id+"commentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPage(id, st, en);
            redisUtil.set("adminarticle_"+id+"commentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("adminarticle_"+id+"commentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> selectCommentPage(int rootId, int id, int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("root"+rootId+"article_"+id+"commentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPageByRoot(rootId, id, st, en);
            redisUtil.set("root"+rootId+"article_"+id+"commentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("root"+rootId+"article_"+id+"commentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> selectCommentPageAll(int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("admincommentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPageAll(st, en);
            redisUtil.set("admincommentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("admincommentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> selectCommentPageAll(int rootId, int st, int en, int num, int pageSize) {
        List<Map<String, Object>> resultList;
        if (redisUtil.get("root"+rootId+"commentPage_"+num+"pageSize_"+pageSize)==null) {
            resultList=tCommentMapper.selectCommentPageAllByRoot(rootId, st, en);
            redisUtil.set("root"+rootId+"commentPage_"+num+"pageSize_"+pageSize,resultList,30);
        }
        else {
            resultList =(List<Map<String, Object>>)redisUtil.get("root"+rootId+"commentPage_"+num+"pageSize_"+pageSize);
        }
        return resultList;
    }
}
