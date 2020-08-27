package com.zsc.blog.mapper;

import com.zsc.blog.entity.TComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public interface TCommentMapper extends BaseMapper<TComment> {
    //分页查询评论
    @Select("select * from t_comment where article_id = #{id} order by created DESC,id DESC limit #{st},#{en};")
    public List<TComment> selectCommentPage(Integer id, Integer st, Integer en);

    /*//通过文章ID删除评论信息
    @Delete("delete from t_comment where article_id = #{id}")
    public void deleteCommentWithid(Integer id);*/

    //通过评论ID删除评论
    @Delete("delete from t_comment where id = #{id}")
    public void deleteCommentWithId(Integer id);

    @Delete("delete from t_comment where article_id = #{id} and author=#{username}")
    public void deleteCommentWithauthor(Integer id,String username);

    @Select("select count(*) from t_comment where article_id = #{id}")
    public int queryCountWithId(Integer id);

    //查询评论数量
    @Select("Select count(*) from t_comment")
    public int queryCount();

    @Select("SELECT t_comment.*,t_article.title from t_comment,t_article where t_comment.author=#{username} and t_article.id=t_comment.article_id")
    public List<Map<String,String>> selcetcommentbyusername(String username);


}
