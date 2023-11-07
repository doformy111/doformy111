package com.zhp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.Utils.RedisCache;
import com.zhp.contents.SystemConstants;
import com.zhp.domain.dto.AddArticleDto;
import com.zhp.domain.dto.ArticleDto;
import com.zhp.domain.entity.ArticleTag;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.Article;
import com.zhp.domain.vo.ArticleDetailVo;
import com.zhp.domain.vo.ArticleListVo;
import com.zhp.domain.vo.HotArticleVo;
import com.zhp.domain.vo.PageVo;
import com.zhp.domain.vo.adminVo.ArticleListAdminVo;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.exception.SystemException;
import com.zhp.mapper.ArticleMapper;
import com.zhp.mapper.ArticleTagMapper;
import com.zhp.service.ArticleService;
import com.zhp.service.ArticleTagService;
import com.zhp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service

public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private CategoryService categoryService;
    @Resource
    private RedisCache redisCache;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量排行查询
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询10条
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();

        List<HotArticleVo> voList = BeanCopyUtils.copyBeanList(articles,HotArticleVo.class);
        return  ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page =new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Article> articleList =page.getRecords();
        articleList.stream()
                .map(article ->  article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())
                        )
               .map(article -> article.setViewCount(Long.valueOf((Integer)redisCache.getCacheMapValue("article:viewCount",
                article.getId().toString()))))
                .collect(Collectors.toList());


        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList,ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        Integer view = redisCache.getCacheMapValue("article:viewCount",id.toString());
        article.setViewCount(Long.valueOf(view));
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long category = articleDetailVo.getCategoryId();
        if (category!=null){
            articleDetailVo.setCategoryName(categoryService.getById(category).getName());
        }




        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAdminList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()),Article::getTitle,articleDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()),Article::getSummary,articleDto.getSummary());
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<ArticleListAdminVo> listAdminVos = BeanCopyUtils.copyBeanList(page.getRecords(),ArticleListAdminVo.class);
        PageVo pageVo =new PageVo(listAdminVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateAdminArticle(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto,Article.class);
        updateById(article);
         articleTagMapper.ArticleRemove(articleDto.getId());
         try {
             List<ArticleTag> articleTags = articleDto.getTags().stream()
                     .map(tagId -> new ArticleTag(article.getId(),tagId))
                     .collect(Collectors.toList());
             articleTagService.saveBatch(articleTags);
         }catch (Exception e){
             throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
         }


        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateAdminArticleContent(Long id) {
        Article byId = getById(id);
        AddArticleDto articleDto = BeanCopyUtils.copyBean(byId,AddArticleDto.class);
        return ResponseResult.okResult(articleDto);
    }

    @Override
    public ResponseResult deleteArticleAdmin(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
