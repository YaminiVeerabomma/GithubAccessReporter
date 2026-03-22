🚀 **GitHub Access Report Service**

A Spring Boot service that connects to GitHub, fetches all repositories of an organization, retrieves collaborators for each repo, and generates a consolidated report showing which users have access to which repositories.
# Github Access Report

A Spring Boot application that fetches all repositories of a GitHub organization along with their collaborators and collaborator roles.

---

## 1. How to Run the Project

1. Clone the repository:

```bash
git clone <your-repo-url>
cd GithubAccessReporter
---
2.Set up environment variables for your GitHub Personal Access Token (PAT):
export GITHUB_TOKEN=<your_github_token>
3.Build and run the Spring Boot application:
mvn clean install
mvn spring-boot:run
4.By default, the app runs on port 8083. You can change it in application.properties:
server.port=8083
spring.application.name=GithubAccessReport
### 2. How Authentication is Configured
+The application uses a GitHub Personal Access Token (PAT) to authenticate API requests.
+The token must have the following scopes:
+repo → to read private repositories (if any)
+read:org → to read organization details
+The token is read frogithub.token=${GITHUB_TOKEN}
github.api.base-url=https://api.github.com
github.org-name=<default_org_name>m an environment variable or from application.properties
+WebClient is configured to include this token in the Authorization header as Bearer <token> for all GitHub API requests.

