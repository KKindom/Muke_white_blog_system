package com.zsc.blog.service;

import com.zsc.blog.entity.TCollect;
import com.zsc.blog.entity.TUser;

import java.util.List;

public interface ITCollectService
{
    //删除收藏根据文章id
    public void Delete(int a_id,int u_id);
    //添加收藏根据文章id
    public void Insert(int a_id, TUser user);
    //查找收藏根据用户名
    public List<TCollect> Select(int uid);

}
