package com.zhj.controller;

import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddArticleDto;
import com.zhj.domin.dto.EditArticleDto;
import com.zhj.domin.dto.QueryArticleDto;
import com.zhj.domin.entity.Article;
import com.zhj.service.ArticleService;
import com.zhj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/18 11:10
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addBlog(@RequestBody AddArticleDto addArticleDto){
        return articleService.addBlog(addArticleDto);
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String title,String summary){
        QueryArticleDto queryArticleDto = new QueryArticleDto(pageNum, pageSize, title, summary);
        return articleService.getArticles(queryArticleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleInfo(@PathVariable Long id){
        return articleService.getArticleInfo(id);
    }

    @PutMapping("/{id}")
    public ResponseResult editStatus(@PathVariable Long id){
        return articleService.editStatus(id);
    }

    @PutMapping
    public ResponseResult editBlog(@RequestBody EditArticleDto editArticleDto){
        return articleService.editBlog(editArticleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }

    @PostMapping("/{id}")
    public ResponseResult editTop(@PathVariable Long id){
        return articleService.editTop(id);
    }
}
