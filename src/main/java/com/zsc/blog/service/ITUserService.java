package com.zsc.blog.service;

import com.zsc.blog.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public interface ITUserService extends IService<TUser> {

     void insert_user(TUser tUser);

    Collection<?> selectList(Object o);

    TUser selectByname(String username);

    TUser selectById(String userId);
}
