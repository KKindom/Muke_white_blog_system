package com.zsc.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.blog.entity.TArticle;
import com.zsc.blog.entity.TCollect;
import org.apache.ibatis.annotations.Delete;

public interface TCollectMapper extends BaseMapper<TCollect>
{
    //根据文章删除收藏记录
    @Delete("delete from t_collect where a_id=#{aid} and u_id=#{uid}")
    public void deletebya_id(int aid,int uid);

    //删除文章时清除关于该文章的收藏记录
    @Delete("delete from t_collect where a_id=#{aid}")
    public void deleteColletWithAid(int aid);

    //删除用户时清除关于该用户的收藏记录
    @Delete("delete from t_collect where u_id=#{uid}")
    public void deleteColletWithUid(int uid);
}
