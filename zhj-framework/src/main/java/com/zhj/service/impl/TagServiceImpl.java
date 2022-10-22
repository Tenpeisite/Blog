package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.TagListDto;
import com.zhj.domin.entity.Tag;
import com.zhj.domin.vo.PageVo;
import com.zhj.mapper.TagMapper;
import com.zhj.service.TagService;
import com.zhj.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-13 16:14:07
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        Page<Tag> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(StringUtils.isNotBlank(tagListDto.getName()),Tag::getName,tagListDto.getName())
                    .or()
                    .like(StringUtils.isNotBlank(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
            page(page,queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        Long userId = SecurityUtils.getUserId();
        tag.setCreateBy(userId);
        tag.setUpdateBy(userId);
        save(tag);
        return ResponseResult.okResult();
    }
}
