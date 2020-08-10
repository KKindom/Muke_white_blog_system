package com.zsc.blog.service;

import com.zsc.blog.entity.TComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
}
