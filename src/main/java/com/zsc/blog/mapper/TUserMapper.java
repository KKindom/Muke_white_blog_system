package com.zsc.blog.mapper;

import com.zsc.blog.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
@Select("Select * from t_user where username=#{username}")
    public TUser selectbyname(String username);

}
