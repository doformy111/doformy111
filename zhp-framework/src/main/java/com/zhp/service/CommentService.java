package com.zhp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.domain.entity.Comment;
import com.zhp.domain.result.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-10-20 14:21:28
 */
public interface CommentService extends IService<Comment> {


    ResponseResult commentList(String s, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

}

