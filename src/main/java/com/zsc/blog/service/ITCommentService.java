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
    public List<Map<String,String>> Selectbyusername(String username);

    //删除评论
    void Delcomment(int id,String username);

    //根据评论ID删除评论
    public void deleteCommentWithId(int id);

    //查询评论数量
    public int queryCommentNumber();

    //根据ID分页查询文章评论
    public List<Map<String, Object>> selectCommentPage(int id, int st, int en, int num, int pageSize);
    public int queryCommentWithId(int id);

    //分页查询所有评论
    public List<Map<String, Object>> selectCommentPageAll(int st, int en, int num, int pageSize);
}
