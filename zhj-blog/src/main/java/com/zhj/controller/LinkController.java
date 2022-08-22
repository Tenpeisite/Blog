package com.zhj.controller;

import com.zhj.domin.Result;
import com.zhj.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/20 17:31
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    public Result getAllLink(){
        return linkService.getAllLink();
    }
}
