package com.zhp.controller;

import com.zhp.domain.dto.AddArticleDto;
import com.zhp.domain.dto.ArticleDto;
import com.zhp.domain.entity.Article;
import com.zhp.domain.result.ResponseResult;
import com.zhp.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return  articleService.add(articleDto);
    }
    @GetMapping("/list")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, ArticleDto articleDto){
        return  articleService.getAdminList(pageNum,pageSize,articleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult updateAdminArticleContent(@PathVariable("id") Long id){
        return articleService.updateAdminArticleContent(id);
    }
    @PutMapping
    public  ResponseResult updateAdminArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateAdminArticle(articleDto);
    }
    @DeleteMapping ("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticleAdmin(id);

    }
}
