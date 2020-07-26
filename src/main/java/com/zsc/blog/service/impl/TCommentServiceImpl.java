package com.zsc.blog.service.impl;

import com.zsc.blog.entity.TComment;
import com.zsc.blog.mapper.TCommentMapper;
import com.zsc.blog.service.ITCommentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mff
 * @since 2020-07-25
 */
@Service
public class TCommentServiceImpl extends ServiceImpl<TCommentMapper, TComment> implements ITCommentService {

}
