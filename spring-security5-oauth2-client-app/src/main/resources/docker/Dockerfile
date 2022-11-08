#
# Used to build OCI-compatible image with buildah and run by podman with non-root user
#
FROM openjdk:19-slim-buster

# load specific set of OS packages for testing (ps,curl,swaks,nc,netstat,jq)
# create non-root user and group
# -l and static IDs assigned to avoid delay in lookups and system logging
ARG THE_USER_ID=1001
ARG THE_GROUP_ID=1001
RUN DEBIAN_FRONTEND=noninteractive && \
  apt-get update && \
  apt-get install -q -y -o Dpkg::Options::="--force-confnew" procps curl swaks netcat net-tools jq && \
  rm -rf /var/lib/apt/lists/* && \
  /usr/sbin/groupadd -g $THE_GROUP_ID spring && \
  /usr/sbin/useradd -l -u $THE_USER_ID -G spring -g $THE_GROUP_ID spring && \
  mkdir logs && chgrp spring logs && chmod ug+rwx logs

# run as non-root
USER spring:spring

# main REST service and OpenAPI /swagger-ui/index.html
EXPOSE 8080

COPY libs/<%=name%>.jar app.jar

CMD ["java","-jar","app.jar"]
