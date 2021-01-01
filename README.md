# Event Arena Service API
Develop Event Arena Service API.

## Technology Stack
- Spring Boot (web, data, cache)
- Gradle
- Database (H2 for testing)
- Java 8
- Log4j2
- Lombok
- Swagger  
- Docker (mysql and redis for caching)

## Compatible IDE
- Netbeans
    - Install Gradle Plugin for Netbeans.
- Intellij
- Eclipse

## Project Configuration
You may check on application.properties file for the default application config.
You may add environment specific config if needed e.g. application-prod.properties

## API Documentation
Firstly, root context is /api, if you want to change that just deal with it on the properties file.

Run bootRun-dev from list of run configuration, once up you can now access the link:
http://localhost:8080/api/swagger-ui/index.html?configUrl=/api/api-docs/swagger-config#/event-controller/getEvent

Below is the list of event endpoints
- POST /api/event/{eventName}
- POST /api/event
- PUT /api/event/{eventId}
- GET /api/events
- GET /api/event/{eventId}
- DELETE /api/event/{eventId}

Edition endpoints
- POST /api/event/{eventId}/edition
- PUT /api/event/{eventId}/edition/{editionId}
- GET /api/event/{eventId}/editions
- GET /api/event/{eventId}/edition/{editionId}
- DELETE /api/event/{eventId}/edition/{editionId}

Race endpoints
- POST /api/event/{eventId}/edition/{editionId}/race
- PUT /api/event/{eventId}/edition/{editionId}/race/{raceId}
- GET /api/event/{eventId}/edition/{editionId}/races
- GET /api/event/{eventId}/edition/{editionId}/race/{raceId}
- DELETE /api/event/{eventId}/edition/{editionId}/race/{raceId

Serie enpoints
- POST /api/event/{eventId}/edition/{editionId}/serie
- PUT /api/event/{eventId}/edition/{editionId}/serie/{serieId}
- GET /api/event/{eventId}/edition/{editionId}/series
- GET /api/event/{eventId}/edition/{editionId}/serie/{serieId}
- DELETE /api/event/{eventId}/edition/{editionId}/serie/{serieId}

## H2 DB for testing only
This is already configured in -dev profile. when running bootRun-dev run configuration h2 will be activated as default db server.
```
spring.datasource.url = jdbc:h2:file:./data/db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

## mysql test profile
This is already configured in -test profile. when running bootRun-test run configuration jdbc mysql will be activated as default db server.
```
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/nextrun2
spring.datasource.username=root
spring.datasource.password=
```
Also you will need to setup mysql in docker and expose respective ports.
 
## Caching test profile (to be configured)
running bootRun-dev run configuration will use default springboot caching implementation, to use redis just configure it in test profile.
- first make sure that redis is running in docker, otherwise run it - docker run -p 16379:6379 -d redis:6.0 redis-server --requirepass "nextrun"

## Docker compose (to be configured)
To be written. for the meantime you can just run containers using docker run command.

## In case app did not stop force it
 - sudo lsof -i :8080
 - kill -9 PID

## Authors
- Mark Anthony Ortiz - ortizmark905@gmail.com

## Reference
