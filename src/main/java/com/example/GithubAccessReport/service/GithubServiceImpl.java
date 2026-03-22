package com.example.GithubAccessReport.service;






import com.example.GithubAccessReport.dto.CollaboratorDTO;
import com.example.GithubAccessReport.dto.RepoDTO;
import com.example.GithubAccessReport.exception.GithubApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    private final WebClient.Builder webClientBuilder;

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.api.base-url}")
    private String githubApiBaseUrl;

    @Override
    public List<RepoDTO> getAllReposWithCollaborators(String orgName) {
        try {
            WebClient client = webClientBuilder
                    .baseUrl(githubApiBaseUrl)
                    .defaultHeader("Authorization", "Bearer " + githubToken)
                    .build();

            // 1. Fetch all repositories
            List<Map<String, Object>> repos = client.get()
                    .uri("/orgs/{org}/repos", orgName)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();

            if (repos == null) return Collections.emptyList();

            // 2. Map repos to RepoDTO
            return repos.stream().map(repoObj -> {
                Map<String, Object> repo = (Map<String, Object>) repoObj; // safe cast
                String repoName = (String) repo.get("name");
                String visibility = (String) repo.get("visibility");

                // 3. Fetch collaborators
                List<Map<String, Object>> collaborators = client.get()
                        .uri("/repos/{org}/{repo}/collaborators", orgName, repoName)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                        .block();

                List<CollaboratorDTO> collaboratorDTOs = (collaborators != null ? collaborators : Collections.emptyList())
                        .stream()
                        .map(collaboratorObj -> {
                            Map<String, Object> collaboratorMap = (Map<String, Object>) collaboratorObj; // cast
                            String username = (String) collaboratorMap.get("login");
                            Map<String, Boolean> permissions = (Map<String, Boolean>) collaboratorMap.get("permissions");
                            String roleName = permissions.entrySet().stream()
                                    .filter(Map.Entry::getValue)
                                    .map(Map.Entry::getKey)
                                    .findFirst()
                                    .orElse("unknown");
                            return new CollaboratorDTO(username, roleName);
                        })
                        .collect(Collectors.toList());

                return new RepoDTO(repoName, visibility, collaboratorDTOs);
            }).collect(Collectors.toList());

        } catch (WebClientResponseException e) {
            throw new GithubApiException("GitHub API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new GithubApiException("Error fetching GitHub data: " + e.getMessage(), e);
        }
    }
}