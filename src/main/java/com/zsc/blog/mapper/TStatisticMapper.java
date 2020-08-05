package com.zsc.blog.mapper;

import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TStatistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

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

    //根据ID删除统计数据
    @Delete("delete from t_statistic where article_id = #{id}")
    public void deleteStatisticWithId(int id);
}
