# **Github Access Report**

A Spring Boot application that fetches **all repositories of a GitHub organization** along with their **collaborators and roles**.

## **1️⃣ How to Run the Project**

**Clone the repository**
git clone <your-repo-url>
cd GithubAccessReporter
**Set your GitHub Personal Access Token (PAT)**
# Replace <your_token> with your actual GitHub token
export GITHUB_TOKEN=<your_token>
**Build and run**
mvn clean install
mvn spring-boot:run
**Note**: Default port = 8083
Change it in application.properties if required.
server.port=8083
spring.application.name=GithubAccessReport

## **2️⃣ Authentication**
The application uses a **GitHub Personal Access Token (PAT)**.
Required scopes:

- repo
- read:org

Configure in application.properties:
github.token=${GITHUB_TOKEN}
github.api.base-url=https://api.github.com
github.org-name=<your-org-name>
Every request automatically includes:
Authorization: Bearer <token>

## **3️⃣ API Endpoint**
**Fetch all repos + collaborators**
GET http://localhost:8083/api/github/repos
**Optional query param**
Param    	Description
org     	GitHub organization name
**Example**
GET http://localhost:8083/api/github/repos?org=my-org
**Sample JSON output**
**Sample JSON output**
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

**Github Token Setup (Classic Token)**:
This project uses a **GitHub Personal Access Token (Classic)** for authentication.

### **Steps to generate token**

1. Go to GitHub → Settings  
2. Navigate to **Developer Settings → Personal Access Tokens → Tokens (classic)**  
3. Click **Generate new token (classic)**  
4. Select scopes:
   - `repo`
   - `read:org`
5. Copy the generated token
 > ⚠️ Save the token securely. You won’t be able to see it again.
## **⚙️ Configure Token in Application**

Set the token as an environment variable:
export GITHUB_TOKEN=ghp_xxxxxxxxxxxxxx

## **4️⃣ Test Using Postman**
1.Open Postman
2.Create a GET request:
http://localhost:8083/api/github/repos
3.Go to Header Select Authorization → Bearer Token
4.Paste your GitHub PAT
5.Click Send



## **5️⃣ Assumptions & Design Choices**
- Uses WebClient for non-blocking requests
- If org param missing → uses default org from properties
- Role is extracted from GitHub permissions
- Returning full list of collaborators per repo
- Custom error handling using GithubApiException
- Supports organizations with 100+ repos and 1000+ users



