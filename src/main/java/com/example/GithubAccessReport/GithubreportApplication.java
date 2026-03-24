package com.example.GithubAccessReport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.GithubAccessReport",          // your controllers, etc.
    "com.example.GithubAccessReportservice"    // your service
})
public class GithubreportApplication {
    public static void main(String[] args) {
        SpringApplication.run(GithubreportApplication.class, args);
    }
}