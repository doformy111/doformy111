package com.zhp.controller;

import com.zhp.annotation.SystemLog;
import com.zhp.domain.result.ResponseResult;
import com.zhp.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @SystemLog(businessNam = "获取友链信息")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}