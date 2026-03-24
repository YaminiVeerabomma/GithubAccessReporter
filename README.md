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

## Caching & Caffeine
🔹 Caching Overview
- Implemented caching to reduce repeated GitHub API calls and improve response time.
- First request fetches data from GitHub and stores it in cache.
- Subsequent requests are served from cache, reducing latency from ~10s to milliseconds.
- TTL (Time-To-Live) is used to automatically refresh cache after 10 minutes, ensuring data stays reasonably up-to-date.

🔹 Why Caffeine Cache
- Lightweight in-memory caching solution for Java/Spring Boot.
- Supports TTL (time-based expiration) and maximum cache size.
- Does not require external setup, unlike Redis.
- Provides fast and thread-safe caching for single-instance applications.
  
🔹 Implementation Details
- Added Spring’s @EnableCaching to enable caching support.
- Used @Cacheable(value = "github_repos", key = "#orgName") on GitHub service method to store API responses.
- Configured Caffeine Cache Manager with:
- expireAfterWrite(10 minutes) → cache automatically expires after 10 minutes
- maximumSize(100) → limits memory usage
```
Caffeine.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .maximumSize(100);
```

**Benefits**:
- Faster API responses for repeated requests
- Reduced GitHub API usage (avoids hitting rate limits)
- Automatic cache expiration without manual intervention
  
**🔹 How to Test**
1.Run the application.
2.Call the GitHub repos API for an organization the first time → data fetched from GitHub (~10s).
3.Call the same API again within 10 minutes → data served from cache (milliseconds).
3.After 10 minutes → cache expires → next call fetches fresh data from GitHub.

##NOTE:
In real-time production scenarios, Redis is preferred for caching due to its distributed nature and persistence.
However, for practical/demo purposes, setting up Redis can be complex, so Caffeine is used as a lightweight, in-memory alternative with TTL support.
