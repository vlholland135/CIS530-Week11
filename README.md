# CIS 530 Week 11: Dockerized Student Microservices

The Week 9 Student microservices system, unchanged in its business logic, packaged as one multi-module Maven project (Option A) and started as a single Docker Compose stack.

| Container | Role | Host port | Container port |
|---|---|---|---|
| service-registry | Eureka Server | 8761 | 8761 |
| student-service | Student records, calls the profile service through OpenFeign | 8081 | 8081 |
| student-profile-service | StudentProfile records | 8082 | 8082 |
| api-gateway | Single entry point for /api/students/** and /api/profiles/** | 8085 | 8080 |
| student-db | MySQL 8.0 for student_db | 3307 | 3306 |
| student-profile-db | MySQL 8.0 for student_profile_db | 3308 | 3306 |

## Project layout

```
CIS530-Week11/
├── pom.xml                      parent aggregator (packaging pom)
├── mvnw, mvnw.cmd, .mvn/        Maven wrapper for building every module from the root
├── docker-compose.yml
├── docker/mysql/
│   ├── student-db/init.sql
│   └── student-profile-db/init.sql
├── service-registry/            Dockerfile + application-docker.properties
├── student-service/             Dockerfile + application-docker.properties
├── student-profile-service/     Dockerfile + application-docker.properties
└── api-gateway/                 Dockerfile + application-docker.properties
```

Each module keeps its Week 9 `application.properties` for local runs. The Docker settings live in `application-docker.properties`, which Compose activates with `SPRING_PROFILES_ACTIVE=docker`. In that profile every `localhost` is replaced by a Compose service name (`student-db`, `student-profile-db`, `service-registry`), because inside a container `localhost` is the container itself.

## Start the stack (Windows PowerShell, from this folder)

```
docker compose down -v --remove-orphans
.\mvnw.cmd clean package -DskipTests
docker compose up --build
```

On macOS or Linux use `./mvnw` in place of `.\mvnw.cmd`.

The MySQL root password defaults to `Springboot@530`. To use another one, set `MYSQL_ROOT_PASSWORD` in your shell or in a `.env` file next to `docker-compose.yml` before running `docker compose up`. The same value is passed to both services as `MYSQL_PASSWORD`.

The init scripts run only when a database volume is first created. If the tables or seed rows are missing, run `docker compose down -v` and start again.

## Check the stack

```
docker compose ps
docker compose ps -a
docker images
docker compose logs -f student-service
```

Eureka dashboard: http://localhost:8761 (STUDENT-SERVICE, STUDENT-PROFILE-SERVICE and API-GATEWAY should show as UP after about a minute).

Requests through the gateway (the root URL http://localhost:8085 returns 404 by design, since no route is defined for `/`):

```
GET  http://localhost:8085/api/students
GET  http://localhost:8085/api/students/1
GET  http://localhost:8085/api/students/1/full
GET  http://localhost:8085/api/profiles
GET  http://localhost:8085/api/profiles/student/1
POST http://localhost:8085/api/students
POST http://localhost:8085/api/profiles
```

`requests.http` holds all of these, including the POST bodies, for the VS Code REST Client extension.

## Tests

The service and controller tests use Mockito and need no database. The `contextLoads` tests in student-service and student-profile-service start the whole application, so they need MySQL. With the Docker stack running, point them at the container databases:

```
$env:MYSQL_PASSWORD="Springboot@530"
.\mvnw.cmd test -pl service-registry,api-gateway
.\mvnw.cmd test -pl student-service "-Dspring.datasource.url=jdbc:mysql://localhost:3307/student_db"
.\mvnw.cmd test -pl student-profile-service "-Dspring.datasource.url=jdbc:mysql://localhost:3308/student_profile_db"
```

If the local MySQL from Week 9 is running on port 3306 with both databases, `.\mvnw.cmd test` from the root runs every module in one pass.

## Changes from the instructor guide

* Boot 4.0.9 and Spring Cloud 2025.1.4 are kept in the parent pom because the Week 9 projects already ran on them.
* The gateway Docker profile uses `spring.cloud.gateway.server.webflux.routes`. Spring Cloud Gateway 5 ignores the older `spring.cloud.gateway.routes` prefix, which would leave every gateway route returning 404.
* `eureka.instance.prefer-ip-address=true` makes each service register its container IP, so the gateway and Feign resolve instances on the Compose network.
* The MySQL password is set once through `MYSQL_ROOT_PASSWORD` instead of being typed into each file, and the health check reads it from the container environment.
