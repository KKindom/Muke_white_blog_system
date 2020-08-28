package com.zsc.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @program: blog_system_f
 * @description: 收藏表
 * @author: Mr.Wang
 * @create: 2020-08-13 22:00
 **/
@Data
public class TCollect extends Model<TCollect>
{
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uId;

    private String username;

    private Integer aId;

    private String title;
    private String photo;
}
