package com.zsc.blog.service;

import com.zsc.blog.entity.TStatistic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 * 带rootId参数的同名函数为root操作，不带id参数的为admin操作
 */
public interface ITStatisticService extends IService<TStatistic> {
//更新文章点击量
    void updatahits(TStatistic tStatistic);
    //更新文章评论
    void updatacom(TStatistic tStatistic);
    //查找文章统计量
    TStatistic findbyid(int id);

    //查询点击量前5的文章
    public List<Map<String, String>> selectArticleWithHits_top5(int rootId);
    public List<Map<String, String>> selectArticleWithHits_top5();

    //查询评论量前5的文章
    public List<Map<String, String>> selectArticleWithComments_top5(int rootId);
    public List<Map<String, String>> selectArticleWithComments_top5();
}
