package com.zsc.blog.service.impl;

import com.zsc.blog.entity.TArticle;
import com.zsc.blog.mapper.TArticleMapper;
import com.zsc.blog.service.ITArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-26
 */
@Service
public class TArticleServiceImpl extends ServiceImpl<TArticleMapper, TArticle> implements ITArticleService {
    @Autowired
    private  TArticleMapper tArticleMapper;
    @Override
    public Collection<?> selectList(Object o) {
        return tArticleMapper.selectList(null);
    }
}
