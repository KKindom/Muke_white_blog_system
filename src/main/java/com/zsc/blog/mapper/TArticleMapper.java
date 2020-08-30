package com.zsc.blog.mapper;

import com.zsc.blog.entity.TArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import javax.websocket.server.ServerEndpoint;
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
public interface TArticleMapper extends BaseMapper<TArticle> {
    //根据页数返回对应页数数据
    @Select("select " +
            "a.id, title as articleName, created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by a.id limit #{st},#{en};")
    public List<Map<String, Object>> selectpagetest(int st, int en);

    //根据页数返回对应页数数据
    @Select("select " +
            "a.id, title as articleName,created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by created DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectnewpagetest(int st, int en);

    //admin和root查询
    @Select("select " +
            "a.id, a.author, title as articleName, comments_num, created as time,hits as pageView,thumbnail as img,categories as type from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by created DESC limit #{st},#{en};")
    public List<Map<String, Object>> adminSelectPage(int st, int en);
    @Select("select " +
            "a.id, a.author, title as articleName, comments_num, created as time,hits as pageView,thumbnail as img,categories as type from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id and a.author = (select username from t_user where id = #{rootId})order by created DESC limit #{st},#{en};")
    public List<Map<String, Object>> adminSelectPageByRoot(int rootId, int st, int en);

    //发表文章
    @Insert("insert into t_article (id, title, content, created, modified, categories, thumbnail) " +
            "values(#{id}, #{title}, #{content}, #{created}, #{modified}, #{categories}, #{thumbnail})")
    public Integer publishArticle(TArticle article);

    //获取新文章id
    @Select("select max(id) + 1 from t_article")
    public Integer getNextId();

    //删除文章
    @Delete("delete from t_article where id = #{id}")
    public void deleteArticleWithId(int id);

    //根据ID查询文章
    @Select("select * from t_article where id = #{id}")
    public TArticle selectArticleWithId(int id);

    //根据ID更新文章
    public void updateArticle(TArticle tArticle);

    //admin查询自己文章数量
    @Select("Select count(*) from t_article where author = (select username from t_user where id = #{adminId})")
    public int queryCount(int adminId);

    //模糊查询文章
    @Select("SELECT id,title,categories,thumbnail FROM t_article WHERE title LIKE'%${con}%' OR content LIKE '%${con}%'")
    public List<Map<String, String>> select_content_withAll(@Param("con") String con);

    //按分类查询文章
    @Select("SELECT id,title,categories,thumbnail FROM t_article WHERE categories=#{type}")
    public  List<Map<String, String>> selectbytype(String type);
    //根据作者搜索作者的文章
    @Select("SELECT id,title,categories,thumbnail, created FROM t_article WHERE author=#{author}")
    public List<Map<String, String>> select_list_withAuthor(String author);


}
