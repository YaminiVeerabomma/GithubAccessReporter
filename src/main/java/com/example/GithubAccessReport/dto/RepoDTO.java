package com.example.GithubAccessReport.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoDTO {
    private String repoName;
    private String visibility;
    private List<CollaboratorDTO> collaborators;
}