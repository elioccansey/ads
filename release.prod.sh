#!/bin/bash
set -e

if [ -z "$1" ] ; then
  echo "Usage: ./release.prod.sh <version>"
  exit 1
fi

VERSION=$1
COMPOSE_FILE=docker-compose.yml
BACKUP_FILE=docker-compose.yml.bak

echo "Backing up and updating docker-compose.yml"
cp $COMPOSE_FILE $BACKUP_FILE

# Use sed to replace lines
sed -i "s|image: ads-backend:.*|image: ads-backend:$VERSION|" $COMPOSE_FILE
#sed -i "s|APP_VERSION: .*|APP_VERSION: $VERSION|" $COMPOSE_FILE

echo "Committing version changes and tagging in Git"
git add pom.xml $COMPOSE_FILE
git commit -m "Release v$VERSION"
git tag v"$VERSION"
git push origin main
git push origin v"$VERSION"

