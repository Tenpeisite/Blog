package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.TagListDto;
import com.zhj.domin.entity.Tag;
import com.zhj.domin.vo.PageVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-10-13 16:14:07
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);
}
