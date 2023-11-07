package com.zhp.controller;

import com.zhp.domain.dto.UserDto;
import com.zhp.domain.result.ResponseResult;
import com.zhp.service.RoleService;
import com.zhp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/system")
@RestController
public class UserController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @GetMapping("/role/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.userList(pageNum,pageSize,roleName,status);
    }
    @GetMapping("/user/list")
    public ResponseResult userList(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
           return userService.getUserList(pageNum,pageSize,userName,phonenumber,status);
    }
    @GetMapping("/role/listAllRole")
    public ResponseResult roleAllList(){
        return roleService.roleAllList();
    }
    @PostMapping("/user")
    public ResponseResult addAdminUser(@RequestBody UserDto userDto){
        return userService.addAdminUser(userDto);

    }}
