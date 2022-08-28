# spring-security5-oauth2-client-app

Spring Boot/Spring Security web app that serves as the "Client Application" entity in an OAuth2 Authorization Code flow.

Full Blog: https://fabianlee.org/2022/08/25/java-spring-security-oauth2-oidc-protecting-client-app-and-resource-server/

Tested with:
* OpenJDK 17
* [Gradle 7.4](https://docs.gradle.org/current/userguide/compatibility.html)
* spring-boot:2.7.3
* spring-security-core:5.7.3
* [Windows 2019 ADFS as Authentication Server](https://fabianlee.org/2022/08/22/microsoft-configuring-an-application-group-for-oauth2-oidc-on-adfs-2019/)

![OAuth2 Entities](https://github.com/fabianlee/spring-boot-security5-oauth2-oidc/raw/main/diagrams/oauth2-entities.drawio.png)
  

## Run using local Docker daemon

```
docker --version

# your ADFS server
export ADFS=win2k19-adfs1.fabian.lee

# OAuth2 client, secret, scope
export ADFS_CLIENT_ID=<the oauth2 client id>
export ADFS_CLIENT_SECRET=<the oauth2 client secret>
export ADFS_SCOPE="openid allatclaims api_delete"

# clear out any older runs
docker rm spring-security5-oauth2-client-app

# run docker image locally, listening on localhost:8080
docker run \
--network host \
-p 8080:8080 \
--name spring-security5-oauth2-client-app \
-e ADFS_CLIENT_ID=$ADFS_CLIENT_ID \
-e ADFS_CLIENT_SECRET=$ADFS_CLIENT_SECRET \
-e ADFS=$ADFS \
-e ADFS_SCOPE="$ADFS_SCOPE" \
fabianlee/spring-security5-oauth2-client-app:1.0
```



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

# start on port 8080, add '--debug-jvm' for JDWP debugging on port 5005
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

## Pull spring-security source for IDE step-through debugging

```
# get proper branch
git clone https://github.com/spring-projects/spring-security.git -b 5.7.x spring-security-src
cd spring-security-src

# get on exact tag
git checkout 5.7.3

# verify, it is OK to be in detatched state
git branch

```

