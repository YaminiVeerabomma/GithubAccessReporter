package com.example.githubreport.dto;

import lombok.Data;
import java.util.List;

@Data
public class RepoAccessDTO {
    private String repoName;
    private List<CollaboratorDTO> collaborators;
}