# TDD demo project for microservice
 
This is a demo application of a microservice using TDD.

This demo consists on a microservice implemented in Spring Boot and Unit and Integration tests with Junit 5.

This service uses a MySQL database for the project and an in-memory H2 database for the integration tests.

## Start the MySQL container

Execute the docker compose file with:

```
docker-compose up
```

After executing it you MySQL instance will be running at __mysql://localhost:3306__ and with credentials informed in __docker-compose.yml__ file.

Also, in order to the database to be persistent no matter how many times you start it, the database file will be stored at __/var/lib/mysql__

## Execute the tests

```
mvn clean install
```

## Start the application

```
mvn spring-boot:run
```

