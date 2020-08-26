package com.zsc.blog.service;

import com.zsc.blog.entity.TCollect;
import com.zsc.blog.entity.TComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public interface ITCommentService extends IService<TComment> {

    Object selectList(Object o);

    //查找评论根据文章id
    List<TComment> SelectByArticle_id(int id);

    //插入评论
    void insertcomment(TComment tComment);

    //查找评论根据用户名
    public List<Map<String,String>> Selectbyuid(String username);

    //删除评论
    void Delcomment(int aid,String username);

    //查询评论数量
    public int queryCommentNumber();
}
