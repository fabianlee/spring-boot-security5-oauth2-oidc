# spring-security5-oauth2-resource-server

Spring Boot/Spring Security web app that serves as the "Resource Server" entity in an OAuth2 Authorization Code flow.

Full Blog: https://fabianlee.org/2022/08/25/java-spring-security-oauth2-oidc-protecting-client-app-and-resource-server/

Tested with:
* OpenJDK 17
* [Gradle 7.4](https://docs.gradle.org/current/userguide/compatibility.html)
* spring-boot:2.7.3
* spring-security-core:5.7.3


![OAuth2 Entities](https://github.com/fabianlee/spring-boot-security5-oauth2-oidc/raw/main/diagrams/oauth2-entities.drawio.png)

## REST endpoints

* GET /infojson - no authentication necessary, returns app info
* GET /api/user/me - returns logged in user info (callee must be logged in)
* GET /api/user - returns list of users (callee must be logged in)
* DELETE /api/user - deletes last user in list (callee must be in managers group and have 'api_delete' scope)
* GET /api/user/engineer - returns list of engineers (callee must be in engineers or managers group)
* GET /api/user/manager - returns list of managers (callee must be in managers group)
  

## Run using host JVM and gradle

```
# need OpenJDK 17+
javac --version
java --version

# your ADFS server
export ADFS=win2k19-adfs1.fabian.lee

# start on port 8081
./gradlew bootRun

```

## Create OCI image with buildah, run with podman

```
./gradlew bootJar
./gradlew buildah
./gradlew podmanRun
```

## Create OCI image with docker, run with docker

```
./gradlew bootJar
./gradlew docker
./gradlew dockerRun

```

