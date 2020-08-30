package com.zsc.blog.service;

import com.zsc.blog.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;
import javafx.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    //删除用户
    public void deleteUserWithId(int it);
    //分页查看用户列表
    public List<TUser> adminSelectUser(int st, int en, int num, int pageSize);

    //验证用户管理员权限
    public Pair<String, Integer> checkPermisson(String token);

    //获取成为管理员申请列表
    public Pair<Integer, List<String>> getRequestList(int pageNo, int pageSize);
    //处理成为管理员申请
    public void processRequest(String username, int type); //type=1为同意，type=0为拒绝
}
