package com.zhj.controller;

import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddLinkDto;
import com.zhj.domin.dto.EditLinkDto;
import com.zhj.domin.dto.EditLinkStatusDto;
import com.zhj.domin.entity.Link;
import com.zhj.domin.vo.LinkVo;
import com.zhj.service.LinkService;
import com.zhj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/25 11:13
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        return linkService.getLinkList(pageNum, pageSize, name, status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto) {
        return linkService.addLink(addLinkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getLink(@PathVariable Long id) {
        Link link = linkService.getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @PutMapping
    public ResponseResult editLink(@RequestBody EditLinkDto editLinkDto) {
        Link link = BeanCopyUtils.copyBean(editLinkDto, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id) {
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody EditLinkStatusDto editLinkStatusDto){
        return linkService.changeLinkStatus(editLinkStatusDto);
    }
}
