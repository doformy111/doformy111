package com.zhp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.Utils.BeanCopyUtils;
import com.zhp.contents.SystemConstants;
import com.zhp.domain.entity.Comment;
import com.zhp.domain.entity.User;
import com.zhp.enums.AppHttpCodeEnum;
import com.zhp.domain.result.ResponseResult;
import com.zhp.domain.vo.CommentVo;
import com.zhp.domain.vo.PageVo;
import com.zhp.exception.SystemException;
import com.zhp.mapper.CommentMapper;
import com.zhp.service.CommentService;
import com.zhp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-10-20 14:21:28
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper,Comment> implements CommentService {
    @Resource
    private UserService userService;


    @Override
    public ResponseResult commentList(String s, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(s),Comment::getArticleId, articleId);
            wrapper.eq(Comment::getRootId, -1);
            wrapper.eq(Comment::getType, s);
            wrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comment = getBaseMapper().selectList(wrapper);
        if (comment.isEmpty()){
            return ResponseResult.okResult();}
        Page<Comment> page =new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<CommentVo> commentVoList = toComment(page.getRecords());

        commentVoList.forEach(commentVo -> {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        });

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments =list(queryWrapper);

        return toComment(comments);
    }

    private List<CommentVo> toComment(List<Comment> list) {
            List<CommentVo> voList = BeanCopyUtils.copyBeanList(list, CommentVo.class);
                voList.stream()
                .forEach(commentVos -> {
                    Long id = commentVos.getCreateBy();
                    User user = userService.getById(id);
                    if (user != null) {
                        commentVos.setUsername(userService.getById(id).getNickName());
                    } else {
                       throw new SystemException(AppHttpCodeEnum.USER_NOT_FIND);
                    }


                    if (commentVos.getToCommentUserId() != -1) {
                        commentVos.setToCommentUserName(userService.getById(commentVos.getToCommentUserId()).getNickName());
                    }
                });
            return  voList;
        }
}

