package com.zhp.service;

import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface BlogLoginService {


    ResponseResult login(User user);

    ResponseResult logout();

}
