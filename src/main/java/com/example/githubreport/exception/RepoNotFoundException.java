package com.example.githubreport.exception;

public class RepoNotFoundException extends RuntimeException {
    public RepoNotFoundException(String repoName) {
        super("Repository not found: " + repoName);
    }
}