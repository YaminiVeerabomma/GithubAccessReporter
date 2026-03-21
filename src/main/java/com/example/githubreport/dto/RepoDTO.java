package com.example.githubreport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RepoDTO {
    private String name;

    // Map GitHub "private" field to visibility
    @JsonProperty("private")
    private boolean isPrivate;

    public String getVisibility() {
        return isPrivate ? "private" : "public";
    }
}