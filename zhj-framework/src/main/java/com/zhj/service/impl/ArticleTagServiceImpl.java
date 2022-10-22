package com.zhj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.domin.entity.ArticleTag;
import com.zhj.mapper.ArticleTagMapper;
import com.zhj.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-19 11:27:32
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
