package com.zsc.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsc.blog.entity.TArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
public interface ITArticleService extends IService<TArticle> {

    Collection<?> selectList(Object o);
    //顺序查询第几页数据
    List<Map<String, Object>> select_page(int st,int en,int num);
    //根据最新时间返回页数据
    List<Map<String, Object>> select_newpage(int st,int en,int num);
    //返回一共有多少条数据
    public List<Map<String, Object>> admin_select_page(int st, int en,int num);
    //管理员后台查询，根据时间排序
    int allarticle();
}
