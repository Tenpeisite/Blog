package com.zhj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.domin.entity.Comment;
import com.zhj.mapper.CommentMapper;
import com.zhj.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-08-22 10:59:42
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
