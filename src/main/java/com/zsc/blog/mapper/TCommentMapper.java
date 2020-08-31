package com.zsc.blog.mapper;

import com.zsc.blog.entity.TComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
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
    //分页查询评论,admin版
    @Select("select * from t_comment where article_id = #{id} order by created DESC,id DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectCommentPage(Integer id, Integer st, Integer en);
    @Select("select b.id, b.created, b.content, status, a.author as articleAuthor, b.author as commentAuthor, title from t_article as a, t_comment as b" +
            " where a.id = b.article_id order by b.created DESC,b.id DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectCommentPageAll(Integer st, Integer en);

    //分页查询,root用户版
    @Select("select * from t_comment as a, t_user as b where a.article_id = #{id} and a.author = b.username and (b.permisson = 'client' or b.id = #{rootId}) " +
            "order by a.created DESC,a.id DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectCommentPageByRoot(Integer rootId, Integer id, Integer st, Integer en);
    @Select("select b.id, b.created, b.content, status, a.author as articleAuthor, b.author as commentAuthor, title from t_article as a, t_comment as b, t_user as c" +
            " where a.id = b.article_id and b.author = c.username and (c.permisson = 'client' or c.id = #{rootId}) order by b.created DESC,b.id DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectCommentPageAllByRoot(Integer rootId, Integer st, Integer en);

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

    //查询评论数量,root用
    @Select("select count(*) from t_comment as a, t_user as b where a.author = b.username and (b.permisson = 'client' or b.id = #{rootId})")
    public int querytCount(Integer rootId);

    //根据文章ID查询评论数量
    @Select("select count(*) from t_comment as a, t_user as b where a.article_id = #{id} and a.author = b.username and (b.permisson = 'client' or b.id = #{rootId})")
    public int queryCountWithAIdByRoot(Integer rootId, Integer id);
    @Select("select count(*) from t_comment where article_id = #{id}")
    public int queryCountWithAId(Integer id);

    //根据用户名返回用户的评论
    @Select("SELECT t_comment.*,t_article.title ,t_user.profilephoto from t_comment,t_article,t_user where t_comment.author=#{username} and t_article.id=t_comment.article_id and t_user.username=#{username}")
    public List<Map<String,String>> selcetcommentbyusername(String username);

    //根据文章id返回用户的评论
    @Select("SELECT t_comment.*,t_user.profilephoto from t_comment,t_user where t_comment.article_id=#{id} and t_comment.author=t_user.username")
    public List<Map<String,String>> selectcomlistby_a_id(Integer id);

    //返回指定范围内的评论  测试用
    @Select("SELECT * FROM t_comment WHERE UNIX_TIMESTAMP(created)  >= UNIX_TIMESTAMP('2020-8-28 0:0:0')  AND  UNIX_TIMESTAMP(created)  <= UNIX_TIMESTAMP('2020-8-30 00:00:00')  ORDER BY id")
    public List<TComment> find();

    //返回评论近几日的评论总数 （有评论的日期才有）  admin用 超管用
    @Select("select DATE_FORMAT(created,'%Y-%m-%d')as date,COUNT(*) as num\n" +
            "FROM t_comment \n" +
            "where content != '' and created != ''\n" +
            "GROUP BY DATE_FORMAT(created,'%Y-%m-%d');")
    public List<Map<String,String>> Selectcommentby_admin();

    //返回评论近几日的评论总数 （有评论的日期才有）  root用 管理员用
    @Select("select DATE_FORMAT(c.created,'%Y-%m-%d')as date,COUNT(*) as num \n" +
            "FROM t_comment as c,t_article  as a\n" +
            "where c.content != '' and c.created != '' and a.id=c.article_id and a.author=#{username} \n" +
            "GROUP BY DATE_FORMAT(c.created,'%Y-%m-%d');")
    public List<Map<String,String>> Selectcommentby_root(String username);


}
