package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.Result;
import com.zhj.domin.entity.Link;
import com.zhj.domin.vo.LinkVo;
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
}
