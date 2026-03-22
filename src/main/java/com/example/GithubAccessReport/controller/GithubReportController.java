
package com.example.GithubAccessReport.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GithubAccessReport.dto.RepoDTO;
import com.example.GithubAccessReport.service.GithubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubReportController {

    private final GithubService githubService;

    @GetMapping("/repos")
    public List<RepoDTO> getRepos(@RequestParam(required = false) String org) {
        return githubService.getAllReposWithCollaborators(org);
    }
}