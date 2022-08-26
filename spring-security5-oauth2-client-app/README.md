# spring-security5-oauth2-client-app

Spring Boot/Spring Security web app that serves as the "Client Application" entity in an OAuth2 Authorization Code flow.

Full Blog: https://fabianlee.org/2022/08/25/java-spring-security-oauth2-oidc-protecting-client-app-and-resource-server/

Tested with:
* OpenJDK 17
* Gradle 7.4 [Java compat](https://docs.gradle.org/current/userguide/compatibility.html)
* spring-boot:2.7.3
* spring-security-core:5.7.3


![OAuth2 Entities](https://github.com/fabianlee/spring-boot-security5-oauth2-oidc/raw/main/diagrams/oauth2-entities.drawio.png)
  

## Run using host JVM and gradle

```
# need OpenJDK 17+
javac --version
java --version

# your ADFS server
export ADFS=win2k19-adfs1.fabian.lee

# OAuth2 client, secret, scope
export ADFS_CLIENT_ID=<the oauth2 client id>
export ADFS_CLIENT_SECRET=<the oauth2 client secret>
export ADFS_SCOPE="openid allatclaims api_delete"

# start on port 8080
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

