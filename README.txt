CIS 530 Week 11 Assignment 11.3: Dockerize the Week 9 Microservices Project

Commands used to start the stack (Windows PowerShell, run from the project root):

  docker compose down -v --remove-orphans
  .\mvnw.cmd clean package -DskipTests
  docker compose up --build

Commands used to check it:

  docker compose ps
  docker compose ps -a
  docker images

Eureka dashboard:  http://localhost:8761
API Gateway:       http://localhost:8085/api/students/1/full

SQL scripts:
  docker/mysql/student-db/init.sql
  docker/mysql/student-profile-db/init.sql

See README.md for the test commands and configuration notes.
