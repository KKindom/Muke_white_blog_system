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
    public List<Map<String, Object>> selectCommentPage(Integer id, Integer st, Integer en);
    @Select("select b.id, b.created, b.content, status, b.author, title from t_article as a, t_comment as b where a.id = b.article_id order by b.created DESC,b.id DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectCommentPageAll(Integer st, Integer en);

    //通过文章ID删除评论信息
    @Delete("delete from t_comment where article_id = #{id}")
    public void deleteCommentWithAid(Integer id);

    //通过评论ID删除评论
    @Delete("delete from t_comment where id = #{id}")
    public void deleteCommentWithId(Integer id);

    //删除用户时删除所有相关评论
    @Delete("delete from t_comment where author=#{username}")
    public void deleteCommentWithUser(String username);

    @Delete("delete from t_comment where id = #{id} and author=#{username}")
    public void deleteCommentWithauthor(Integer id,String username);

    @Select("select count(*) from t_comment where article_id = #{id}")
    public int queryCountWithId(Integer id);

    //查询评论数量
    @Select("Select count(*) from t_comment")
    public int queryCount();

    //根据用户名返回用户的评论
    @Select("SELECT t_comment.*,t_article.title ,t_user.profilephoto from t_comment,t_article,t_user where t_comment.author=#{username} and t_article.id=t_comment.article_id and t_user.username=#{username}")
    public List<Map<String,String>> selcetcommentbyusername(String username);

    //根据文章id返回用户的评论
    @Select("SELECT t_comment.*,t_user.profilephoto from t_comment,t_user where t_comment.article_id=#{id} and t_comment.author=t_user.username")
    public List<Map<String,String>> selectcomlistby_a_id(Integer id);

}
