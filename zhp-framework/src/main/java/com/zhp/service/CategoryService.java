package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.Category;
import com.zhp.domain.vo.CategoryVo;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-10-16 20:15:49
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCatogoryList();


    ResponseResult showAllCategory(Integer pageNum, Integer pageSize,String name,String status);

    ResponseResult addCategory(CategoryVo categoryVo);

    ResponseResult deleteCategory(Long id);

    ResponseResult getOneCategory(Long id);

    ResponseResult updatCategory(CategoryVo categoryVo);
}

