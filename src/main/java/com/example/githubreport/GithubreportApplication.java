package com.example.githubreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.githubreport")


public class GithubreportApplication {
    public static void main(String[] args) {
        SpringApplication.run(GithubreportApplication.class, args);
    }
}