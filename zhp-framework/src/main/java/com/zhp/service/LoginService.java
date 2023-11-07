package com.zhp.service;

import com.zhp.domain.entity.User;
import com.zhp.domain.result.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {


    ResponseResult login(User user);

    ResponseResult logout();

}
