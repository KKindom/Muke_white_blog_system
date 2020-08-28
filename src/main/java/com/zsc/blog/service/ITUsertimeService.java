package com.zsc.blog.service;

import com.zsc.blog.entity.TUsertime;

public interface ITUsertimeService
{
    //根据日期和用户id查看是否存在记录
    public TUsertime findusertime(int uid,String data);
    //插入新的时间记录
    public void inserttime(TUsertime tUsertime);
    //更新时间记录
    public void updatatime(TUsertime tUsertime);
    //更新阅读记录数
    public void updatanum(TUsertime tUsertime);
}
