#!/bin/bash
#
# Calculates new semantic veresion (e.g. v1.0.1) tag, builds Java Spring BootJar for release,
# generates release notes based on git commits, then creates new Github Release
#

# check for Github CLI binaries
gh_bin=$(which gh)
[ -n "$gh_bin" ] || { echo "ERROR Github CLI binary not available, https://fabianlee.org/2022/04/21/github-cli-tool-for-repository-operations/"; exit 2; }
# check for java compiler
java_bin=$(which javac)
[ -n "$java_bin" ] || { echo "ERROR Java compiler not available, 'sudo apt search openjdk-*; sudo apt install openjdk-xx-jdk'"; exit 2; }

# 
# CALCULATE NEW SEMANTIC TAG (vX.Y.Z)
#
if git tag --sort=committerdate | grep -q ^v ; then

  # get latest semantic version tag, construct patch+1
  semantic_version=$(git tag --sort=-committerdate | grep ^v | grep -Po '^v[0-9]*.[0-9]*.[0-9]*' | head -n1)
  [ -n "$semantic_version" ] || { echo "ERROR could not find semantic version vX.Y.Z"; exit 3; }

  major_minor=$(echo "$semantic_version" | cut -d'.' -f1-2)
  patch=$(echo "$semantic_version" | cut -d'.' -f3)
  ((patch++))
  newtag="${major_minor}.${patch}"
else
  semantic_version=""
  newtag="v1.0.1"
fi
echo "old version: $semantic_version new_version: ${newtag}"
docker_version=${newtag//v/}
echo "docker_version: $docker_version"


#
# GRADLE BUILD OF SPRING BOOTJAR
#

# construct bootJar and local image, then push to docker hub
./gradlew bootJar
echo "Spring BootJar built"
./gradlew buildah buildahPush -PdockerVersion=$docker_version
echo "Images pushed to Docker hub"


#
# GENERATE RELEASE NOTES FROM GIT COMMITS
#
if [ -n "$semantic_version" ]; then
  git log HEAD...${semantic_version} --pretty="- %s " > /tmp/$newtag.log
  [ $? -eq 0 ] || { echo "ERROR could not retrieve logs for 'git log HEAD...${semantic_version}"; exit 7; }
else
  git log --pretty="- %s " > /tmp/$newtag.log
fi



#
# PUSH, COMMIT NEW TAG, CREATE RELEASE
#
echo ""
echo "== RELEASE $newtag =================================="
cat /tmp/$newtag.log 
echo "===================================="
echo ""
read -p "Push this new release $newtag [y/n]? " -i n -e answer
if [[ "$answer" == "y" ]]; then
  set -x
  git commit -a -m "changes for new tag $newtag"
  git tag $newtag && git push origin $newtag
  git push
  gh release create $newtag -F /tmp/$newtag.log spring-security5-oauth2-resource-server/build/libs/spring-security5-oauth2-resource-server.jar spring-security5-oauth2-client-app/build/libs/spring-security5-oauth2-client-app.jar
  set +x
else
  echo "aborted release creation"
fi
