package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.dto.AddLinkDto;
import com.zhj.domin.dto.EditLinkStatusDto;
import com.zhj.domin.entity.Link;
import com.zhj.domin.vo.LinkVo;
import com.zhj.domin.vo.PageVo;
import com.zhj.mapper.LinkMapper;
import com.zhj.service.LinkService;
import com.zhj.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-08-20 17:35:29
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper,Link> implements LinkService {

    @Override
    public Result getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_status_normal);
        List<Link> list = list(queryWrapper);
        //转换成Vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list, LinkVo.class);
        return Result.okResult(linkVos);
    }

    @Override
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        Page<Link> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Link::getName,name)
                .eq(StringUtils.isNotBlank(status),Link::getStatus,status);
        page(page,queryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos,page.getTotal()));
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(EditLinkStatusDto editLinkStatusDto) {
        Link link = getById(editLinkStatusDto.getId());
        link.setStatus(editLinkStatusDto.getStatus());
        updateById(link);
        return ResponseResult.okResult();
    }
}
