package com.zhp.job;

import com.zhp.Utils.RedisCache;
import com.zhp.domain.entity.Article;
import com.zhp.service.ArticleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0 */10 * * * ?")
    public void updateViewCount(){
        Map<String,Integer> viewCount = redisCache.getCacheMap("article:viewCount");
        List<Article> articles =  viewCount.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()),entry.getValue().longValue()) )
                .collect(Collectors.toList());

        articleService.updateBatchById(articles);
    }
}
