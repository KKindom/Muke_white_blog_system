package com.zsc.blog.mapper;

import com.zsc.blog.entity.TUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mff
 * @since 2020-07-25
 */
public interface TUserMapper extends BaseMapper<TUser> {
@Select("select * from t_user where username={#name}")
   public TUser selectByname(String name);
}
