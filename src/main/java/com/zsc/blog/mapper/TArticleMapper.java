package com.zsc.blog.mapper;

import com.zsc.blog.entity.TArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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
public interface TArticleMapper extends BaseMapper<TArticle> {
    //根据页数返回对应页数数据
    @Select("select " +
            "a.id ,title as articleName,created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by a.id limit #{st},#{en};")
    public List<Map<String, Object>> selectpagetest(int st, int en);

    //根据页数返回对应页数数据
    @Select("select " +
            "a.id ,title as articleName,created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by created DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectnewpagetest(int st, int en);

    //管理员查询
    @Select("select " +
            "a.id ,title as articleName, comments_num, created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by created DESC limit #{st},#{en};")
    public List<Map<String, Object>> admin_selectPage(int st, int en);

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
}
