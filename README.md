## spring-boot-security5-oauth2-oidc

blog: https://fabianlee.org/2022/08/25/java-spring-security-oauth2-oidc-protecting-client-app-and-resource-server/

Java Spring Boot/Spring Security implementations of OAuth2 entities:

* [Client Application, web app running on port 8080](spring-security5-oauth2-client-app/README.md)
* [Resource Server, microservice running on port 8081](spring-security5-oauth2-resource-server/README.md)

Using:
* OpenJDK 17
* [Gradle 7.4](https://docs.gradle.org/current/userguide/compatibility.html)
* Spring Boot 2.7.3
* Spring Security 5.7.3
* [Windows 2019 ADFS as Authentication Server](https://fabianlee.org/2022/08/22/microsoft-configuring-an-application-group-for-oauth2-oidc-on-adfs-2019/)

![OAuth2 Entities](https://github.com/fabianlee/spring-boot-security5-oauth2-oidc/raw/main/diagrams/oauth2-entities.drawio.png)


## Root project, create OCI images for subprojects with buildah

```
./gradlew bootJar
./gradlew buildah [-PdockerVersion=1.0.1 ]
```

## Root project, create OCI images for subprojects with docker

```
./gradlew bootJar
./gradlew docker [-PdockerVersion=1.0.1 ]

```

# Creating tag

```
newtag=v1.0.1
git commit -a -m "changes for new tag $newtag" && git push
git tag $newtag && git push origin $newtag
```

# Deleting tag

```
todel=v1.0.1

# delete local tag, then remote
git tag -d $todel && git push origin :refs/tags/$todel
```

# Deleting release

```
todel=v1.0.1

# delete release and remote tag
gh release delete $todel --cleanup-tag -y

# delete local tag
git tag -d $todel
```

