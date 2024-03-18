Frontend repo: https://github.com/halo8880/sharevideo-fe <br>
## Project introduction
This is a simple project that allows users to share Youtube video URLs with each other. The backend service will fetch the video information and store it in the database. The frontend app will display the video information and allow users to play the video. The system also supports real-time notification when a new video is shared.
## Tech stack
Backend Tech used: Java, Spring framework, Postgres, redis (as message broker and cache database), JWT authentication, websocket<br>
Frontend tech: React, Material-UI <br>
Important versions: `Java 17`,` React 18`, `postgreSQL 11`, `Redis 7` , `Spring boot 3` <br>
Deployment: Docker, docker-compose <br>
Development tools: Maven 3.8.3 <br>

## High level design

![img.png](img.png)

**Due to tight deadline and having spent too much time focusing on design and backend service, the frontend is not as good as I expected.** <br>

## How to deploy locally
how to run locally using docker:
1. make sure you have docker and docker-compose installed
2. clone the repo
3. cd into docker/deliver: `cd docker/deliver`
4. run `docker-compose up redis postgres -d` and wait for some seconds to make sure those services are up and running
5. run `docker-compose up backend frontend -d` to start the remaining services
6. The frontend app should be running at `http://localhost:3000`
7. The backend app should be running at `http://localhost:8081`

## How to run development mode
### Backend:
##### postgres credentials:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: user
1. make sure you have docker, Java 17
2. cd into docker/deliver: `cd docker/deliver`
3. run `docker-compose up redis postgres -d` and wait for some seconds to make sure those services are up and running
4. cd into root dir again: `cd ../../`
5. modify connection configs: in file `src/main/resources/application.properties`:<br>
         change `spring.datasource.url=jdbc:postgresql://postgres:5432/mydb` to `spring.datasource.url=jdbc:postgresql://localhost:5432/mydb` <br>
         change `redis.host=redis` to `redis.host=localhost` <br>
6. For linux/mac: run `./mvnw spring-boot:run` to start the backend app
6. For windows: run `./mvnw.cmd spring-boot:run` to start the backend app
7. If get error at step 6, check JAVA_HOME environment variable, it should point to the root of your JDK installation
8. The backend app should be running at `http://localhost:8081`

### Frontend:
1. cd into frontend dir
2. run `npm install` to install all the dependencies
3. run `npm start` to start the frontend app
4. The frontend app should be running at `http://localhost:3000`

## How to use: <br>
After signing in, there will be a button to share Youtube video URLs, the system current support 3 formats:
1. https://www.youtube.com/watch?v=videoId
2. https://youtu.be/videoId
3. https://youtube.com/videoId

You won't be able to share same video twice. <br>
The news feed will be updated once a new video is shared. <br>
Will need to login again after refreshing the page. <br>
If you enter a not existed username to login, it will automatically register that username/password account and do the login <br>
A use is treated as offline if last activity is more than 5 minutes. (configurable in `app.lastSeenThresholdInSeconds`) <br>

## How to run tests
Included the tests for important classes and methods. <br>
### Backend:
1. for linux/mac: run `./mvnw test` to run all the tests
2. for windows: run `./mvnw.cmd test` to run all the tests