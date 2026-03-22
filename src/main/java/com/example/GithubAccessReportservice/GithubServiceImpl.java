package com.example.GithubAccessReportservice;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.GithubAccessReport.dto.CollaboratorDTO;
import com.example.GithubAccessReport.dto.RepoDTO;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    private final WebClient.Builder webClientBuilder;

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.api.base-url}")
    private String githubBaseUrl;

    @Value("${github.org-name}")
    private String githubOrg;

    @Override
    public List<RepoDTO> getAllReposWithCollaborators(String orgName) {
        String org = (orgName != null && !orgName.isEmpty()) ? orgName : githubOrg;
        WebClient client = webClientBuilder.build();

        // 1. Fetch all repos (Visibility is automatically mapped if RepoDTO has the field)
        List<RepoDTO> repos = client.get()
                .uri(githubBaseUrl + "/orgs/{org}/repos", org)
                .headers(h -> h.setBearerAuth(githubToken))
                .retrieve()
                .bodyToFlux(RepoDTO.class)
                .collectList()
                .block();

        if (repos != null) {
            return repos.stream().map(repo -> {
                // 2. Fetch collaborators
                List<CollaboratorDTO> collaborators = client.get()
                        .uri(githubBaseUrl + "/repos/{org}/{repo}/collaborators", org, repo.getName())
                        .headers(h -> h.setBearerAuth(githubToken))
                        .retrieve()
                        .bodyToFlux(CollaboratorDTO.class)
                        .collectList()
                        .block();

                // 3. Process the "null" permissions into a readable string
                if (collaborators != null) {
                    collaborators.forEach(c -> c.setPermission(c.getPermission()));
                }

                repo.setCollaborators(collaborators);
                return repo;
            }).collect(Collectors.toList());
        }

        return List.of();
    }
}

//@Service
//@RequiredArgsConstructor
//public class GithubServiceImpl implements GithubService {
//
//    private final WebClient.Builder webClientBuilder;
//
//    @Value("${github.token}")
//    private String githubToken;
//
//    @Value("${github.api.base-url}")
//    private String githubBaseUrl;
//
//    @Value("${github.org-name}")
//    private String githubOrg;
//
//    @Override
//    public List<RepoDTO> getAllReposWithCollaborators(String orgName) {
//        String org = (orgName != null && !orgName.isEmpty()) ? orgName : githubOrg;
//
//        WebClient client = webClientBuilder.build();
//
//        // Fetch all repos for the org
//        List<RepoDTO> repos = client.get()
//                .uri(githubBaseUrl + "/orgs/{org}/repos", org)
//                .headers(headers -> headers.setBearerAuth(githubToken))
//                .retrieve()
//                .bodyToFlux(RepoDTO.class)
//                .collectList()
//                .block();
//
//        if (repos != null) {
//            return repos.stream().map(repo -> {
//                // Fetch collaborators for each repo
//                List<CollaboratorDTO> collaborators = client.get()
//                        .uri(githubBaseUrl + "/repos/{org}/{repo}/collaborators", org, repo.getName())
//                        .headers(headers -> headers.setBearerAuth(githubToken))
//                        .retrieve()
//                        .bodyToFlux(CollaboratorDTO.class)
//                        .collectList()
//                        .block();
//
//                repo.setCollaborators(collaborators);
//                return repo;
//            }).collect(Collectors.toList());
//        }
//
//        return List.of();
//    }
//}