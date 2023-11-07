package com.zhp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhp.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-27 14:19:11
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    void ArticleRemove(Long articleId);
}
