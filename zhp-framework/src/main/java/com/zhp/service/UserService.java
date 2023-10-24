package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.entity.User;
import com.zhp.domain.result.ResponseResult;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-10-20 15:46:08
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

}

