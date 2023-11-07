package com.zhp.controller;

import com.zhp.annotation.SystemLog;
import com.zhp.domain.dto.TagListDto;
import com.zhp.domain.entity.Comment;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.PageVo;
import com.zhp.domain.vo.TagVo;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.zhp.service.TagService ;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping
    public ResponseResult addTag(String name,String remark){

        return tagService.addPageTag(name,remark);
    }
    @DeleteMapping("/{id}")
    @SystemLog(businessNam = "删除标签")
    public   ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteThisTage(id);
    }

    @GetMapping ("/{id}")
    @SystemLog(businessNam = "修改标签显示信息")
    public  ResponseResult displayTag(@PathVariable("id")Long id){
        return tagService.displayTag(id);
    }

    @PutMapping
    @SystemLog(businessNam = "修改标签")
    public  ResponseResult updateTag(@RequestBody TagVo tagVo){
        return tagService.updateTage(tagVo);
    }



    @GetMapping("/listAllTag")
    public ResponseResult getlistAllTag(){
        return tagService.getlistAllTag();
    }
}