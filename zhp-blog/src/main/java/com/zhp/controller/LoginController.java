package com.zhp.controller;

import com.zhp.annotation.SystemLog;
import com.zhp.domain.entity.User;
import com.zhp.domain.result.ResponseResult;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.exception.SystemException;
import com.zhp.service.BlogLoginService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Resource
    private BlogLoginService blogLoginService;
    @PostMapping("/login")
    @SystemLog(businessNam = "用户登录")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }
    @PostMapping("/logout")
    @SystemLog(businessNam = "用户退出登录")
    public ResponseResult logout(){
       return blogLoginService.logout();

    }
}