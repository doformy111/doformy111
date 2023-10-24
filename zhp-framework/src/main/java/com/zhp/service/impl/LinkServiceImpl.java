package com.zhp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.contents.SystemConstants;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.entity.Link;
import com.zhp.domain.vo.LinkVo;
import com.zhp.mapper.LinkMapper;
import com.zhp.service.LinkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-10-18 11:23:59
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links,LinkVo.class);

        //封装返回
        return  ResponseResult.okResult(linkVos);
    }
}

