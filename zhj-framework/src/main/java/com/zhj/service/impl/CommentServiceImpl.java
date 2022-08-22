package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.Result;
import com.zhj.domin.entity.Comment;
import com.zhj.domin.entity.User;
import com.zhj.domin.vo.CommentVo;
import com.zhj.domin.vo.PageVo;
import com.zhj.mapper.CommentMapper;
import com.zhj.service.CommentService;
import com.zhj.service.UserService;
import com.zhj.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-08-22 10:59:42
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public Result commentList(Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        //1.构造查询条件
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId)
                .eq(Comment::getRootId, SystemConstants.COMMENT_STATUS_NORMAL);
        //分页查询
        Page<Comment> pageInfo = new Page<>(pageNum, pageSize);
        page(pageInfo, queryWrapper);
        //拷贝对象
        List<CommentVo> commentVoList = toCommentVoList(pageInfo.getRecords());
        //查询所有根评论对应的子评论集合，并赋值
        commentVoList = commentVoList.stream().map(item -> {
            //获得子评论列表vo
            List<CommentVo> children = getChildren(item.getId());
            item.setChildren(children);
            return item;
        }).collect(Collectors.toList());
        return Result.okResult(new PageVo(commentVoList, pageInfo.getTotal()));
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        commentVoList = commentVoList.stream().map(item -> {
            //通过createdBy查询用户的昵称并赋值
            User user = userService.getById(item.getCreateBy());
            item.setUsername(user.getNickName());
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才查询
            if (item.getToCommentUserId().intValue() != SystemConstants.COMMENT_STATUS_NORMAL) {
                String nickName = userService.getById(item.getToCommentUserId()).getNickName();
                item.setToCommentUserName(nickName);
            }
            return item;
        }).collect(Collectors.toList());
        return commentVoList;
    }

    /**
     * 根据根评论id查询所对应的子评论集合
     *
     * @param id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        //查询对应子评论id
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1.1构造条件
        commentLambdaQueryWrapper.eq(Comment::getRootId, id)
                .orderByAsc(Comment::getCreateTime);
        List<Comment> list = list(commentLambdaQueryWrapper);
        //2.封装子评论列表vo
        List<CommentVo> commentVos = toCommentVoList(list);
        return commentVos;
    }
}
