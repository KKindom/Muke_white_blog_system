package com.zsc.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @program: blog_system_f
 * @description: 用户每日记录
 * @author: Mr.Wang
 * @create: 2020-08-27 15:09
 **/
@Data
public class TUsertime extends Model<TUsertime>
{    @TableId(value = "id", type = IdType.AUTO)
    int id;
    int uId;
    String created;
    int time;
    int num;
    public   TUsertime (int uid,String created)
    {
        this.created=created;
        this.uId=uid;
        this.time=0;
        this.num=0;
    }
    public TUsertime(){
        super();
    }

}
