package com.zsc.blog.mapper;

import com.zsc.blog.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public interface TUserMapper extends BaseMapper<TUser> {
    //根据uername查找用户
    @Select("Select * from t_user where username=#{username}")
    public TUser selectbyname(String username);

    //查询用户数量
    @Select("Select count(*) from t_user")
    public int queryCount();
}
