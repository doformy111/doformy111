package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.dto.TagListDto;
import com.zhp.domain.entity.Tag;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.PageVo;
import com.zhp.domain.vo.TagVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-10-24 15:01:06
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addPageTag(String name,String remark);



    ResponseResult deleteThisTage(Long id);





    ResponseResult displayTag(Long id);





    ResponseResult updateTage(TagVo tagVo);


    ResponseResult getlistAllTag();

    ResponseResult getAllCategory();
}

