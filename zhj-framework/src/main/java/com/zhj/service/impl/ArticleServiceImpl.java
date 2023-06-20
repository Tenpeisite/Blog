package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddArticleDto;
import com.zhj.domin.dto.EditArticleDto;
import com.zhj.domin.dto.QueryArticleDto;
import com.zhj.domin.entity.Article;
import com.zhj.domin.Result;
import com.zhj.domin.entity.ArticleTag;
import com.zhj.domin.entity.Category;
import com.zhj.domin.vo.ArticleDetailVo;
import com.zhj.domin.vo.ArticleVo;
import com.zhj.domin.vo.HotArticleVo;
import com.zhj.domin.vo.PageVo;
import com.zhj.mapper.ArticleMapper;
import com.zhj.service.ArticleService;
import com.zhj.service.ArticleTagService;
import com.zhj.service.CategoryService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.RedisCache;
import org.assertj.core.util.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/19 10:16
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //1.必须是正式发布的
        //2.按照浏览量排序
        //3.最多查询前10条
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Article> records = page.getRecords();
        //List<HotArticleVo> list = records.stream().map(item->{
        //    HotArticleVo hotArticleVo = new HotArticleVo();
        //    BeanUtils.copyProperties(item,hotArticleVo);
        //    return hotArticleVo;
        //}).collect(Collectors.toList());
        List<HotArticleVo> list = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        list = list.stream().map(item -> {
            Integer viewCount = (Integer) redisCache.getCacheMap(SystemConstants.REDIS_VIEWCOUNT_PREFIX).get(item.getId().toString());
            item.setViewCount(Long.valueOf(viewCount));
            return item;
        }).collect(Collectors.toList());
        return Result.okResult(list);
    }

    @Override
    public Result articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //1.查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null && categoryId > 0, Article::getCategoryId, categoryId)
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);
        //2.分页查询
        Page<Article> pageInfo = new Page<>(pageNum, pageSize);
        page(pageInfo, queryWrapper);
        Page<ArticleVo> articleVoPage = new Page<>();
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, articleVoPage, "records");
        List<ArticleVo> list = pageInfo.getRecords().stream().map(item -> {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(item, articleVo, "categoryId");
            Category category = categoryService.getById(item.getCategoryId());
            articleVo.setCategoryName(category.getName());
            Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEWCOUNT_PREFIX, item.getId().toString());
            articleVo.setViewCount(viewCount.longValue());
            return articleVo;
        }).collect(Collectors.toList());
        PageVo pageVo = new PageVo(list, articleVoPage.getTotal());
        return Result.okResult(pageVo);
    }

    @Override
    public Result getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEWCOUNT_PREFIX, id.toString());
        article.setViewCount(viewCount.longValue());
        //封装vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        return Result.okResult(articleDetailVo);
    }

    @Override
    public Result updateViewCount(Long id) {
        //Article article = getById(id);
        //article.setViewCount(article.getViewCount()+1);
        //updateById(article);
        //return Result.okResult();

        //更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.REDIS_VIEWCOUNT_PREFIX, id.toString(), 1);
        return Result.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addBlog(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加博客和标签的关联关系
        articleTagService.saveBatch(articleTags);

        //把图片url存入redis
        redisTemplate.opsForSet().add(SystemConstants.Article_PIC_DB_RESOURCES,article.getThumbnail());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticles(QueryArticleDto queryArticleDto) {
        Page<Article> page = new Page<>(queryArticleDto.getPageNum(), queryArticleDto.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryArticleDto.getTitle()), Article::getTitle, queryArticleDto.getTitle())
                .or()
                .like(StringUtils.isNotBlank(queryArticleDto.getSummary()), Article::getSummary, queryArticleDto.getSummary())
                .orderByDesc(Article::getCreateTime);
        page(page, queryWrapper);
        List<ArticleDetailVo> articleDetailVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleDetailVo.class);
        return ResponseResult.okResult(new PageVo(articleDetailVos, page.getTotal()));
    }

    @Override
    public ResponseResult getArticleInfo(Long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, id);
        List<Long> tags = articleTagService.list(queryWrapper).stream().map(item -> item.getTagId()).collect(Collectors.toList());
        article.setTags(tags);
        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult editStatus(Long id) {
        Article article = getById(id);
        //查看状态，是“0”就变成“1”，是“1”就变成“0”
        String status = article.getStatus();
        if (SystemConstants.ARTICLE_STATUS_NORMAL.equals(status)) {
            //如果已发布就改成未发布
            article.setStatus(SystemConstants.ARTICLE_STATUS_DRAFT);
        } else {
            //如果未发布就改成已发布
            article.setStatus(SystemConstants.ARTICLE_STATUS_NORMAL);
        }
        updateById(article);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult editBlog(EditArticleDto editArticleDto) {
        Article article = BeanCopyUtils.copyBean(editArticleDto, Article.class);
        updateById(article);
        //删除之前的标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(queryWrapper);
        //新增标签
        List<ArticleTag> tags = article.getTags().stream()
                .map(item -> new ArticleTag(article.getId(), item))
                .collect(Collectors.toList());
        articleTagService.saveBatch(tags);

        redisTemplate.opsForSet().add(SystemConstants.Article_PIC_DB_RESOURCES,editArticleDto.getThumbnail());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        Article article = getById(id);
        removeById(id);
        //获得文章id来删除对应标签关系
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(queryWrapper);
        redisTemplate.opsForSet().remove(SystemConstants.Article_PIC_DB_RESOURCES,article.getThumbnail());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult editTop(Long id) {
        //改变置顶状态
        //获得该文章
        Article article = getById(id);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsTop, SystemConstants.ARTICLE_IS_TOP);
        Article one = getOne(queryWrapper);
        if (one == null) {
            //如果one为null，说明没有文章置顶,则直接把该文章置顶
            article.setIsTop(SystemConstants.ARTICLE_IS_TOP);
        } else {
            if (one.getId().equals(article.getId())) {
                //如果one和article是同一篇文章的话，将其置顶取消
                article.setIsTop(SystemConstants.ARTICLE_ISNOT_TOP);
            } else {
                //将其他文章的置顶取消
                one.setIsTop(SystemConstants.ARTICLE_ISNOT_TOP);
                updateById(one);
                //将article设为置顶
                article.setIsTop(SystemConstants.ARTICLE_IS_TOP);
            }
        }
        updateById(article);
        return ResponseResult.okResult();
    }
}
