---
layout: page
title: Project Documentation
---

<h1>Design Overview</h1>
The project contains 2 Spring Boot modules, namely **MainServer** and **SecondaryServer**. **Main Server** handles the bulk 
of API calls for CRUD functionality, while **SecondaryServer** is used to simulate 
a nested server call.

<h1>Opening The Project (Intellij) </h1>
Open the parent pom.xml at /MB-SimpleSpringbootApp/pom.xml and open as project.
Maven should load the 2 child modules automatically.

<h1>Rest API</h1>

For more details on the expected JSON requests and responses, refer to the Postman collection included in the 
git repo.

<h4>Admin Functions</h4>

| API Url       | Method        |         Notes          |
|:-------------:|:-------------:|:----------------------:|
| /admin/ok     | GET             | For server healthcheck |

<h4>CRUD Functions</h4>

|              API Url              | Method |                          Notes                          |
|:---------------------------------:|:------:|:-------------------------------------------------------:|
|      /api/v1/customer/create      |  POST  |              Create a new Customer entity               |
| /api/v1/customer/update/{userId}  |  PUT   |            Updates a Customer entity detail             |
| /api/v1/customer/list?page={page} |  GET   |    Paginated list of Customer entities (10 per page)    |
|    /api/v1/customer/linkDevice    |  POST  | Create a new Device entity, and link it to the Customer |

<h4>Nested API Functions</h4>

| API Url       | Method        |         Notes          |
|:-------------:|:-------------:|:----------------------:|
| /api/v1/customer/genToken   | POST             | Calls **SecondaryServer** to generate a token for a given Customer and Device |

<h1>Request & Response Logging</h1>
A custom WebMvcConfigurer is used to intercept incoming requests and outgoing responses.
This enables a single logging configuration for all controllers, reducing the amount of repeated code in
order to log requests and responses. **MainServer** logs are written to /MB-SimpleSpringbootApp/logs/main-server.log.

<h1>Nested API Call</h1>
For the genToken functionality, the **MainServer** received the API call and repackages
the DTO. It then uses a Spring WebClient to send an async request to the **SecondaryServer**. As the 'token generation' is a dummy
function, **SecondaryServer** simply concatenates the userId with the deviceId. The **MainServer** then retrieves the response,
blocks when the value is needed, and returns the result.

<h1>Error Handling</h1>
Server errors such as userId not found are handled by intercepting the custom Exception classes, then using Spring's ResponseStatusException
to respond to the API caller. The server.error.include-message property is set to always for verbose error message for the API caller.

<h1>Database</h1>
Default database setup in application.properties is for MS SQL. As I was developing on a Mac machine, Docker was utilised to run
a MS SQL DB.

To setup a Docker MS SQL DB for this project, run:

```console
docker run --cap-add SYS_PTRACE -e 'ACCEPT_EULA=1' -e 'MSSQL_SA_PASSWORD=12qwQW!@' -p 1433:1433 --name mb_mainserver -d mcr.microsoft.com/mssql/server:2022-latest
```

For Macs, due to the lack of ARM64 container, ensure Rosetta is enabled in Docker Desktop settings.

<h1>Unit Tests</h1>
Simple unit tests were created to ensure core business logic did not break throughout development of the service layer.