package com.example.githubreport.service;

import com.example.githubreport.dto.RepoDTO;
import com.example.githubreport.dto.RepoAccessDTO;
import java.util.List;

public interface GithubService {

    List<RepoDTO> getAllRepos();

    RepoAccessDTO getRepoCollaborators(String repoName);
}