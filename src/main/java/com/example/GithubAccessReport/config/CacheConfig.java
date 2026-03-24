package com.example.GithubAccessReport.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("github_repos");

        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(2, TimeUnit.MINUTES) // TTL 10 mins
                        .maximumSize(100)
        );

        return cacheManager;
    }
}