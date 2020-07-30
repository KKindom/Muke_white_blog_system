package com.zsc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsc.blog.Utils.RedisUtil;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TUserMapper;
import com.zsc.blog.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {
    @Autowired
    TUserMapper tUserMapper;
    @Resource
    RedisUtil redisUtil;
    //查询所有用户
    @Override
    public Collection<?> selectList(Object o) {
        return tUserMapper.selectList(null);
    }
    //根据名字查询
    @Override
    public TUser selectByusername(String username) {
//        QueryWrapper<TUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(TUser::getUsername,username);
        return tUserMapper.selectbyname(username);
    }
    //根据id查询
    @Override
    public TUser selectById(String userId) {
        return tUserMapper.selectById(userId);
    }
    //插入用户
    @Override
    public void insert_user(TUser tUser)
    {
        redisUtil.set(tUser.getUsername(),tUser);
        tUserMapper.insert(tUser);
    }
}
