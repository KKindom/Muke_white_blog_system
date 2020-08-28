package com.zsc.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TStatistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

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
}
