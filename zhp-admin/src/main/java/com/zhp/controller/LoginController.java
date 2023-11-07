package com.zhp.controller;

import com.zhp.Utils.BeanCopyUtils;
import com.zhp.Utils.SecurityUtils;
import com.zhp.annotation.SystemLog;
import com.zhp.domain.entity.LoginUser;
import com.zhp.domain.entity.Menu;
import com.zhp.domain.entity.User;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.AdminUserInfoVo;
import com.zhp.domain.vo.RoutersVo;
import com.zhp.domain.vo.UserInfoVo;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.exception.SystemException;
import com.zhp.service.BlogLoginService;
import com.zhp.service.LoginService;
import com.zhp.service.MenuService;
import com.zhp.service.RoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LoginController {
    @Resource
    private LoginService loginService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;
    @PostMapping("/user/login")
    @SystemLog(businessNam = "用户登录")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());

                //根据用户id查询角色信息
         List<String> roles = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        User user= loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roles,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);

    }
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回

        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
   @SystemLog(businessNam = "用户退出登录")
        public ResponseResult logout(){
        return loginService.logout();

  }
}