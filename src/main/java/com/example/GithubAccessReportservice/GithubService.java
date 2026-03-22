
package com.example.GithubAccessReportservice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GithubAccessReport.dto.RepoDTO;

@Service
public interface GithubService {
    List<RepoDTO> getAllReposWithCollaborators(String usernameOrOrg);
}