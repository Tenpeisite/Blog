package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.Result;
import com.zhj.domin.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-08-22 10:59:42
 */
public interface CommentService extends IService<Comment> {

    Result commentList(Long articleId, Integer pageNum, Integer pageSize);
}
