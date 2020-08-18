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
}
