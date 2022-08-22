package com.zhj.controller;

import com.zhj.domin.Result;
import com.zhj.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/19 10:05
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //@GetMapping("/list")
    //public List<Article> test(){
    //    return articleService.list();
    //}

    /**
     * 查询热门文章
     * @return
     */
    @GetMapping("/hotArticleList")
    public Result hotArticleList(){
        //查询热门文章，封装成result返回
        Result result=articleService.hotArticleList();
        return result;
    }

    /**
     * 文章列表分页查询
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/articleList")
    public Result articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 修改阅读量
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public Result updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
