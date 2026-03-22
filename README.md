## Overview

**GitHub Access Report** is a Spring Boot service that retrieves **all repositories in a GitHub organization** and returns each repository’s **collaborators** and **permission roles** (admin, maintain, write, triage, read).

## Features

- Fetches repositories for a given organization
- Fetches collaborators for each repository
- Includes collaborator permission level per repository
- Uses **Spring WebClient** for non-blocking GitHub API calls
- Centralized error handling via `GithubApiException`

## Prerequisites

- Java 17+ (or your project’s configured Java version)
- Maven 3.8+
- A **GitHub Personal Access Token (Classic)**
    - Scopes:
        - `repo`
        - `read:org`

## Configuration

You can configure the app using environment variables plus `application.properties`.

### 1) Create a token (classic)

1. GitHub → **Settings**
2. **Developer settings** → **Personal access tokens** → **Tokens (classic)**
3. **Generate new token (classic)**
4. Select scopes: `repo`, `read:org`
5. Copy the token

> ⚠️ Save the token securely. You won’t be able to view it again.
> 

### 2) Export the token

```bash
export GITHUB_TOKEN=<your_token>
```

### 3) [application.properties](http://application.properties)

```
# App
server.port=8083
spring.application.name=GithubAccessReport

# GitHub
github.token=${GITHUB_TOKEN}
github.api.base-url=https://api.github.com
github.org-name=<your-default-org>
```

## Run locally

### Clone

```bash
git clone <your-repo-url>
cd GithubAccessReporter
```

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8083` (unless you changed `server.port`).

## API

### Get repositories + collaborators

**Endpoint**

```
GET /api/github/repos
```

**Optional query parameter**

- `org` (string): GitHub organization name. If omitted, the app uses `github.org-name` from configuration.

**Examples**

```bash
curl "http://localhost:8083/api/github/repos"
curl "http://localhost:8083/api/github/repos?org=my-org"
```

**Sample response**

```json
[
  {
    "repoName": "example-repo",
    "visibility": "public",
    "collaborators": [
      {
        "username": "johnDoe",
        "roleName": "admin"
      },
      {
        "username": "janeDoe",
        "roleName": "write"
      }
    ]
  }
]
```

## Test with Postman

1. Create a **GET** request:
    - `http://localhost:8083/api/github/repos`
2. Authorization:
    - Type: **Bearer Token**
    - Token: your GitHub PAT
3. Send the request

## Notes / Assumptions

- If `org` is not provided, the app uses the default org from configuration.
- Permission role is derived from GitHub collaborator permissions.
- Designed to handle organizations with large numbers of repositories and users (pagination is required for real-world orgs).

## Troubleshooting

- **401 Unauthorized**: token is missing/invalid or the required scopes are not selected.
- **403 Forbidden**: rate limit exceeded or insufficient access to the org/repo.
- **404 Not Found**: org name is wrong or the token cannot access the org.

## Security

- Do not commit tokens to source control.
- Prefer environment variables or secret managers in production.
