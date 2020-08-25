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
    //插入用户
     void insert_user(TUser tUser);
    //查找用户
    Collection<?> selectList(Object o);
    //查找用户根据名字
    TUser selectByusername(String username);
    //查找用户根据id
    TUser selectById(String userId);
    //更新用户信息
    void updata_I(TUser tUser);
    //返回用户名数量
    int find_usercount(String usrname);
    //查询用户数量
    public int queryUserNumber();
}
