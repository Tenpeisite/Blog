package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.dto.AddLinkDto;
import com.zhj.domin.dto.EditLinkStatusDto;
import com.zhj.domin.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-08-20 17:35:29
 */
public interface LinkService extends IService<Link> {

    Result getAllLink();

    ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult changeLinkStatus(EditLinkStatusDto editLinkStatusDto);
}
