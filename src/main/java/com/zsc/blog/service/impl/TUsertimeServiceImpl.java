package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TUsertime;
import com.zsc.blog.mapper.TUsertimeMapper;
import com.zsc.blog.service.ITUsertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blog_system_f
 * @description:
 * @author: Mr.Wang
 * @create: 2020-08-27 15:20
 **/
@Service
public class TUsertimeServiceImpl implements ITUsertimeService {
   @Autowired
    TUsertimeMapper tUsertimeMapper;
    @Resource
    RedisUtil redisUtil;
    @Override
    public TUsertime findusertime(int uid, String data) {
        TUsertime tUsertime=tUsertimeMapper.selectOne(new QueryWrapper<TUsertime>().eq("u_id",uid).eq("created",data));

        return tUsertime;
    }

    @Override
    public void inserttime(TUsertime tUsertime) {
        tUsertimeMapper.insert(tUsertime);
    }

    @Override
    public void updatatime(TUsertime tUsertime) {
        tUsertimeMapper.updateById(tUsertime);
        System.out.println("更新时间成功！");
    }

    @Override
    public void updatanum(TUsertime tUsertime) {
        tUsertimeMapper.updateById(tUsertime);
        System.out.println("更新文章计数成功！");

    }
}
