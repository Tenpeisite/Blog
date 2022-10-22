package com.zhj.controller;

import com.zhj.constants.SystemConstants;
import com.zhj.domin.Result;
import com.zhj.domin.dto.AddComentDto;
import com.zhj.domin.entity.Comment;
import com.zhj.service.CommentService;
import com.zhj.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/22 13:52
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public Result commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_TYPE,articleId,pageNum,pageSize);
    }

    @PostMapping
    public Result addComment(@RequestBody AddComentDto addComentDto){
        Comment comment = BeanCopyUtils.copyBean(addComentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表",notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    public Result linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.YOULIAN_TYPE,null,pageNum,pageSize);
    }
}
