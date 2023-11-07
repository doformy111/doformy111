package com.zhp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.contents.SystemConstants;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.Article;
import com.zhp.domain.entity.Category;

import com.zhp.domain.vo.CategoryVo;
import com.zhp.domain.vo.PageVo;
import com.zhp.mapper.CategoryMapper;
import com.zhp.service.ArticleService;
import com.zhp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-10-16 20:15:56
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCatogoryList() {
        //查询文章表  状态为已发布的文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categoryList = listByIds(categoryIds);
        categoryList = categoryList
                .stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult showAllCategory(Integer pageNum, Integer pageSize, String name,String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<CategoryVo> voList  = BeanCopyUtils.copyBeanList(page.getRecords(),CategoryVo.class);

        return ResponseResult.okResult(new PageVo(voList,page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryVo categoryVo) {
        Category category = BeanCopyUtils.copyBean(categoryVo,Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getOneCategory(Long id) {
        Category byId = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(byId,CategoryVo.class);

        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updatCategory(CategoryVo categoryVo) {
        Category category = BeanCopyUtils.copyBean(categoryVo,Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

}
