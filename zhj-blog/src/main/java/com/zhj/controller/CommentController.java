package com.zhj.controller;

import com.zhj.constants.SystemConstants;
import com.zhj.domin.Result;
import com.zhj.domin.entity.Comment;
import com.zhj.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/22 13:52
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public Result commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_TYPE,articleId,pageNum,pageSize);
    }

    @PostMapping
    public Result addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public Result linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.YOULIAN_TYPE,null,pageNum,pageSize);
    }
}
