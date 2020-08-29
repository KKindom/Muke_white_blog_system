package com.zsc.blog.mapper;

import com.zsc.blog.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
    @Select("Select count(*) from t_user where permisson='cilent'")
    public int queryCount();

    //删除用户
    @Delete("Delete * from t_user where id=#{id}")
    public void deleteUser(int id);

    //查询用户信息
    @Select("select * from t_user where permisson='cilent' order by id DESC limit #{st},#{en};")
    public List<TUser> selectUser(int st, int en);
}
