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
 * 带rootId参数的同名函数为root操作，不带id参数的为admin操作
 */
public interface ITCommentService extends IService<TComment> {

    Object selectList(Object o);

    //查找评论根据文章id
    List<Map<String ,String >> SelectByArticle_id(int id);

    //插入评论
    void insertcomment(TComment tComment);

    //查找评论根据用户名
    public List<Map<String,String>> Selectbyusername(String username);

    //删除评论
    void Delcomment(int id,String username);

    //根据评论ID删除评论
    public void deleteCommentWithId(int id);

    //查询所有评论数量
    public int queryCommentNumber();
    public int queryCommentNumber(int rootId);

    //根据ID分页查询文章评论
    public List<Map<String, Object>> selectCommentPage(int id, int st, int en, int num, int pageSize);
    public List<Map<String, Object>> selectCommentPage(int rootId, int id, int st, int en, int num, int pageSize);
    public int queryCommentWithAId(int id);
    public int queryCommentWithAId(int rootId, int id);

    //分页查询所有评论
    public List<Map<String, Object>> selectCommentPageAll(int st, int en, int num, int pageSize);
    public List<Map<String, Object>> selectCommentPageAll(int rootId, int st, int en, int num, int pageSize);
}
