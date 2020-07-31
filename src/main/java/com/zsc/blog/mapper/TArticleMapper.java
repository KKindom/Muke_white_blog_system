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
    public List<Map<String, Object>> selectpagetest(int st, int en);

    //根据页数返回对应页数数据
    @Select("select " +
            "a.id ,title as articleName,created as time,hits as pageView,thumbnail as img from t_article as a,t_statistic as b  " +
            "where a.id=b.article_id order by created DESC limit #{st},#{en};")
    public List<Map<String, Object>> selectnewpagetest(int st, int en);

    //管理员查询
    @Select("select a.id ,d.pageView, d.comments_num, title as articleName,created as time,modified as modified, thumbnail as img from t_article as a " +
            "left join (select hits as pageView, b.comments_num as comments_num, c.article_id from t_statistic as c " +
            "left join (select count(*) as comments_num, article_id from t_comment group by article_id) as b on c.article_id = b.article_id group by c.article_id) " +
            "as d on a.id = d.article_id order by created DESC limit #{st},#{en};"
    )
    public List<Map<String, Object>> admin_selectPage(int st, int en);
}
