package com.zhp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.domain.dto.TagListDto;
import com.zhp.domain.entity.Category;
import com.zhp.domain.entity.Tag;
import com.zhp.domain.result.ResponseResult;

import com.zhp.domain.vo.CategoryVo;
import com.zhp.domain.vo.PageVo;
import com.zhp.domain.vo.TagVo;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.exception.SystemException;
import com.zhp.mapper.TagMapper;
import com.zhp.service.CategoryService;
import com.zhp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-10-24 15:01:06
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    CategoryService categoryService;


    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper <Tag> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
//        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemake()),Tag::getRemark,tagListDto.getRemake());
        queryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page =new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Tag> list = page.getRecords();
        List<TagVo> voList = BeanCopyUtils.copyBeanList(list, TagVo.class);
        PageVo pageVo = new PageVo(voList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addPageTag(String name,String remark) {
        if(!StringUtils.hasText(name)){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        if(!StringUtils.hasText(remark)){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        Tag tag = new Tag(name,remark);


        save(tag);
        return ResponseResult.okResult();
    }



    @Override
    public ResponseResult deleteThisTage(Long id) {
           removeById(id);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult displayTag(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTage(TagVo tagVo) {
        Tag tag = getById(tagVo.getId());
        tag.setName(tagVo.getName());
        tag.setRemark(tagVo.getRemark());
        updateById(tag);
        return ResponseResult.okResult();
    }



    @Override
    public ResponseResult getlistAllTag() {
        List<Tag> tagList = list();
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(tagList,TagVo.class);
        return ResponseResult.okResult(tagVoList);
    }
    @Override
    public ResponseResult getAllCategory() {
        List<Category> category = categoryService.list();
        List<CategoryVo>categoryVoList = BeanCopyUtils.copyBeanList(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVoList);
    }

}

