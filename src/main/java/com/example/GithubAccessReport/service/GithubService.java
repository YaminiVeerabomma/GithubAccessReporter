
package com.example.GithubAccessReport.service;



import java.util.List;

import com.example.GithubAccessReport.dto.RepoDTO;

public interface GithubService {
    List<RepoDTO> getAllReposWithCollaborators(String usernameOrOrg);
}