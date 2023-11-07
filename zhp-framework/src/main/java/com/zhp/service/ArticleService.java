package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.dto.AddArticleDto;
import com.zhp.domain.dto.ArticleDto;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult getAdminList(Integer pageNum, Integer pageSize,ArticleDto articleDto );


    ResponseResult updateAdminArticle(AddArticleDto article);

    ResponseResult updateAdminArticleContent(Long id);

    ResponseResult deleteArticleAdmin(Long id);

}
