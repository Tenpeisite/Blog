package com.zhj.controller;

import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddTagDto;
import com.zhj.domin.dto.TagListDto;
import com.zhj.domin.dto.UpdateTagDto;
import com.zhj.domin.entity.Tag;
import com.zhj.domin.vo.PageVo;
import com.zhj.domin.vo.TagVo;
import com.zhj.service.TagService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/13 16:17
 */
@RestController
@RequestMapping("/content/tag")
@Api(tags = "标签",description = "标签相关接口")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody UpdateTagDto updateTagDto){
        Tag tag = BeanCopyUtils.copyBean(updateTagDto, Tag.class);
        Long userId = SecurityUtils.getUserId();
        tag.setUpdateBy(userId);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.addTag(tag);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagInfo(@PathVariable Integer id){
        Tag tag = tagService.getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @DeleteMapping("/{ids}")
    public ResponseResult delTag(@PathVariable List<Integer> ids){
        tagService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<Tag> list = tagService.list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }

}
