package com.zhp.controller;

import com.zhp.annotation.SystemLog;
import com.zhp.domain.entity.User;
import com.zhp.domain.result.ResponseResult;
import com.zhp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @SystemLog(businessNam = "获取用户信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }
    @PutMapping("/userInfo")
    @SystemLog(businessNam = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    @SystemLog(businessNam = "用户注册")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
