package com.zhp.controller;

import com.zhp.annotation.SystemLog;
import com.zhp.domain.result.ResponseResult;
import com.zhp.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
   private ArticleService articleService;

    @GetMapping("/hotArticleList")
    @SystemLog(businessNam = "获取最热文章信息")
    public ResponseResult  hotArticleList(){
            ResponseResult result = articleService.hotArticleList();
            return  result;

    }
    @GetMapping("/articleList")
    @SystemLog(businessNam = "获取首页最热十篇文章信息")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }
    @GetMapping("/{id}")
    @SystemLog(businessNam = "查看某文章详细信息")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }
    @PutMapping("/updateViewCount/{id}")
    @SystemLog(businessNam = "更新文章查看次数")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

}
