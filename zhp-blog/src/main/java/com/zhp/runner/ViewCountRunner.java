package com.zhp.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhp.Utils.RedisCache;
import com.zhp.domain.entity.Article;
import com.zhp.mapper.ArticleMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        List<Article> list = articleMapper.selectList(null);
       Map<String,Integer>  map=list.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(),
                        article ->  article.getViewCount().intValue()
                        ));
       redisCache.setCacheMap("article:viewCount",map);

    }
}
