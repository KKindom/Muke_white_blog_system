package com.zsc.blog.service;

import com.zsc.blog.entity.TStatistic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public interface ITStatisticService extends IService<TStatistic> {
//更新文章点击量
    void updatahits(TStatistic tStatistic);
    //更新文章评论
    void updatacom(TStatistic tStatistic);
    //查找文章统计量
    TStatistic findbyid(int id);
}
