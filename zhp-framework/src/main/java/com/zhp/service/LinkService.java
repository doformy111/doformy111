package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.Link;
import com.zhp.domain.vo.LinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-10-18 11:24:14
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult addLink(LinkVo linkVo);

    ResponseResult showAllLink(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult deleteLike(Long id);
}

