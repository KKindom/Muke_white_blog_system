package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TStatistic;
import com.zsc.blog.mapper.TStatisticMapper;
import com.zsc.blog.service.ITStatisticService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
@Service
public class TStatisticServiceImpl extends ServiceImpl<TStatisticMapper, TStatistic> implements ITStatisticService {
   @Autowired
   TStatisticMapper tStatisticMapper;
    @Resource
    RedisUtil redisUtil;

    public TStatisticServiceImpl() {
        super();
    }

    @Override
    public void updatahits(TStatistic tStatistic) {
        tStatisticMapper.updateById(tStatistic);
    }

    @Override
    public void updatacom(TStatistic tStatistic) {

        tStatisticMapper.updateById(tStatistic);

    }

    @Override
    public TStatistic findbyid(int id) {
      return   tStatisticMapper.selectOne(new QueryWrapper<TStatistic>().eq("article_id",id));
    }
}
