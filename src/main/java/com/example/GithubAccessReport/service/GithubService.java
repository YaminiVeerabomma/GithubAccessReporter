
package com.example.GithubAccessReport.service;



import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.example.GithubAccessReport.dto.RepoDTO;

@Cacheable(value = "github_repos", key = "#orgName")
public interface GithubService {
    List<RepoDTO> getAllReposWithCollaborators(String usernameOrOrg);
}