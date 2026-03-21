package com.example.githubreport.controller;

import com.example.githubreport.dto.*;
import com.example.githubreport.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubReportController {

    private final GithubService githubService;

    @GetMapping("/repos")
    public List<RepoDTO> getRepos() {
        return githubService.getAllRepos();
    }

    @GetMapping("/repos/{repoName}/collaborators")
    public RepoAccessDTO getRepoCollaborators(@PathVariable String repoName) {
        return githubService.getRepoCollaborators(repoName);
    }
}