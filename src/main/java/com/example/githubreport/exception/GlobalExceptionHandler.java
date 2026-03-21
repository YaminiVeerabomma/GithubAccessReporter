package com.example.githubreport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiError> handleGithubApi(
            WebClientResponseException ex,
            WebRequest request
    ) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                ex.getStatusCode().value(),           // ✅ FIXED
                "GitHub API Error",
                ex.getResponseBodyAsString(),         // better error message
                request.getDescription(false)
        );

        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    @ExceptionHandler(RepoNotFoundException.class)
    public ResponseEntity<ApiError> handleRepoNotFound(
            RepoNotFoundException ex,
            WebRequest req
    ) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Repository Not Found",
                ex.getMessage(),
                req.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}