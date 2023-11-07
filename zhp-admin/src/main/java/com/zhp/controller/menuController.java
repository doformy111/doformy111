package com.zhp.controller;

import com.zhp.domain.result.ResponseResult;
import com.zhp.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/menu")
public class menuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult listAdmin(String status,String menuName){
                return menuService.getList(status,menuName);
    }
}
