package com.zsc.blog.entity;

import lombok.Data;

import java.sql.Date;

/**
 * @program: blog_system_f
 * @description: 分页实体类
 * @author: Mr.Wang
 * @create: 2020-07-29 22:48
 **/
@Data
public class Page_article
{
    int id;
    String articleName;
    Date time;
    int pageView;
    String img;

}
