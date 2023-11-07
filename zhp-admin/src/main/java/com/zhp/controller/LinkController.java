package com.zhp.controller;

import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.LinkVo;
import com.zhp.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @PostMapping
    public ResponseResult addLink(@RequestBody LinkVo linkVo){
        return   linkService.addLink(linkVo);
    }
    @GetMapping("/list")
    public ResponseResult showAllLink(Integer pageNum,Integer pageSize,String name,String status){
        return  linkService.showAllLink(pageNum,pageSize,name,status);
    }
    @DeleteMapping("/{id}")
    public  ResponseResult deleteLink(@PathVariable("id") Long id){
        return linkService.deleteLike(id);
    }
 }
