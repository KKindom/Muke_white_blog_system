package com.zsc.blog.mapper;

import com.zsc.blog.entity.Page_article;
import com.zsc.blog.entity.TArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    public   List <Page_article>  selectpage(int st,int en);

    //根据页数返回对应页数数据
    @Select("select " +
            "a.id ,title as articleName,created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by created DESC limit #{st},#{en};")
    public   List <Page_article>  selectnewpage(int st,int en);

    //根据页数返回对应页数数据
    @Select("select " +
            "a.id ,title as articleName,created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by a.id limit #{st},#{en};")
    public   List <Map<String,Object>>  selectpagetest(int st,int en);

    //根据页数返回对应页数数据
    @Select("select " +
            "a.id ,title as articleName,created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by created DESC limit #{st},#{en};")
    public   List <Map<String,Object>>  selectnewpagetest(int st,int en);
}
