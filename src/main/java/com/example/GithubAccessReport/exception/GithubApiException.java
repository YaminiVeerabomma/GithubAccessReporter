package com.example.GithubAccessReport.exception;



public class GithubApiException extends RuntimeException {

    public GithubApiException() {
        super();
    }

    public GithubApiException(String message) {
        super(message);
    }

    public GithubApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public GithubApiException(Throwable cause) {
        super(cause);
    }
}
