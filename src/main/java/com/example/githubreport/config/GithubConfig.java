package com.example.githubreport.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class GithubConfig {

    @Value("${github.token}")
    private String token;

    @Value("${github.org-name}")
    private String organization;

    @Value("${github.api.base-url}")
    private String baseUrl;
}