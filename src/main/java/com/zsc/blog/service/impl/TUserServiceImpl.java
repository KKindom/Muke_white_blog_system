package com.zsc.blog.service.impl;

import com.zsc.blog.entity.TUser;
import com.zsc.blog.mapper.TUserMapper;
import com.zsc.blog.service.ITUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-25
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {
    @Autowired
    TUserMapper tUserMapper;
    @Override
    public TUser selectByname(String name) {
        return tUserMapper.selectByname(name);
    }
}
