package com.example.githubreport.service;

import com.example.githubreport.dto.*;
import com.example.githubreport.exception.RepoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    private final WebClient.Builder webClientBuilder;

    @Value("${github.api.base-url}")
    private String baseUrl;

    @Value("${github.token}")
    private String token;

    @Value("${github.org-name}")
    private String orgName;

    private WebClient client() {
        return webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Accept", "application/vnd.github+json")
                .build();
    }

    @Override
    public List<RepoDTO> getAllRepos() {
        List<RepoDTO> repos;
        try {
        	
            repos = client().get()
                    .uri("/orgs/" + orgName + "/repos")
                    .retrieve()
                    .bodyToFlux(RepoDTO.class)
                    .collectList()
                    .block();

            System.out.println("Fetched Repos: " + repos);
        } catch (WebClientResponseException ex) {
            System.err.println("GitHub API Error: " + ex.getResponseBodyAsString());
            throw ex;
        }

        return repos;
    }

    @Override
    public RepoAccessDTO getRepoCollaborators(String repoName) {
        List<CollaboratorDTO> collaborators;

        try {
            collaborators = client().get()
                    .uri("/repos/" + orgName + "/" + repoName + "/collaborators")
                    .retrieve()
                    .bodyToFlux(CollaboratorDTO.class)
                    .collectList()
                    .block();

            System.out.println("Collaborators for repo " + repoName + ": " + collaborators);

        } catch (WebClientResponseException.NotFound ex) {
            throw new RepoNotFoundException(repoName);
        } catch (WebClientResponseException ex) {
            System.err.println("GitHub API Error: " + ex.getResponseBodyAsString());
            throw ex;
        }

        RepoAccessDTO dto = new RepoAccessDTO();
        dto.setRepoName(repoName);
        dto.setCollaborators(collaborators);
        return dto;
    }
}