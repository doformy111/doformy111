package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-10-18 11:24:14
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}

