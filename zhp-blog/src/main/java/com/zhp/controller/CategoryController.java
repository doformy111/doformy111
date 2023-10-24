package com.zhp.controller;

import com.zhp.annotation.SystemLog;
import com.zhp.domain.entity.User;
import com.zhp.domain.result.ResponseResult;
import com.zhp.service.BlogLoginService;
import com.zhp.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @GetMapping("/getCategoryList")
    @SystemLog(businessNam = "获取文章分类信息")
    public ResponseResult getCategoryList(){

        return categoryService.getCatogoryList();
    }

}
