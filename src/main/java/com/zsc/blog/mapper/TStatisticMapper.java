package com.zsc.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TStatistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

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
public interface TStatisticMapper extends BaseMapper<TStatistic> {
    //新增文章对应的统计信息
    @Insert("insert into t_statistic(id, article_id, hits, comments_num) values(#{id}, #{id}, 0, 0)")
    public void addStatistic(TArticle article);

    //根据文章ID删除统计数据
    @Delete("delete from t_statistic where article_id = #{id}")
    public void deleteStatisticWithId(int id);

    //删除评论时更新统计数据
    @Update("update t_statistic set comments_num = comments_num - 1 where article_id = #{id}")
    public void updateStatisticAfterDeleteComment(int id);

    //删除用户时更新文章评论数据
    @Update("update t_statistic as a set comments_num = (select count(*) from t_comment as b where a.article_id = b.article_id)")
    public void updateStatisticComment();

    //查询所有文章中点击量前5 超管用
    @Select("select article_id,hits,t_article.title from t_statistic,t_article where article_id = t_article.id order by hits DESC limit 0,5;")
    public List<Map<String,String>> selectArticleWithHits_top5();
    //查询所有文章中评论量前5 超管用
    @Select("select article_id,comments_num,t_article.title from t_statistic,t_article where article_id = t_article.id order by comments_num DESC limit 0,5;")
    public List<Map<String,String>> selectArticleWithComments_top5();

    //查询作者文章点击量前5   管理员用
    @Select("select article_id,hits,t_article.title from t_statistic,t_article where article_id = t_article.id and t_article.author=#{username} order by hits DESC limit 0,5;")
    public List<Map<String,String>> selectArticleWithHitsByRoot_top5(String username);
    //查询作者文章点击量前5   管理员用
    @Select("select article_id,comments_num,t_article.title from t_statistic,t_article where article_id = t_article.id and t_article.author=#{username} order by comments_num DESC limit 0,5;")
    public List<Map<String,String>> selectArticleWithCommentsByRoot_top5(String username);

}
