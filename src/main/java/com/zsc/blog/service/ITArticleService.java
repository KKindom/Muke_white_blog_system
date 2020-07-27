package com.zsc.blog.service;

import com.zsc.blog.entity.TArticle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

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
}
