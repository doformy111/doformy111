package com.zhp.controller;

import com.zhp.annotation.SystemLog;
import com.zhp.contents.SystemConstants;
import com.zhp.domain.entity.Comment;
import com.zhp.domain.result.ResponseResult;
import com.zhp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @SystemLog(businessNam = "获取文章下用户评论")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @PostMapping
    @SystemLog(businessNam = "增加用户评论")
    public ResponseResult addComment(@RequestBody Comment comment){
        return  commentService.addComment(comment);
    }
    @GetMapping("/linkCommentList")
    @SystemLog(businessNam = "获取友链下用户信息")
    public ResponseResult linkCommentList( Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
}}