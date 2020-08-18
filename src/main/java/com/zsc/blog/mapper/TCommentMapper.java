package com.zsc.blog.mapper;

import com.zsc.blog.entity.TComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public interface TCommentMapper extends BaseMapper<TComment> {
    //通过文章ID删除评论信息
    @Delete("delete from t_comment where article_id = #{id}")
    public void deleteCommentWithId(Integer id);

    @Delete("delete from t_comment where article_id = #{id} and author=#{username}")
    public void deleteCommentWithauthor(Integer id,String username);

}
