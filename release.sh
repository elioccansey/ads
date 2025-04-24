#!/bin/bash
set -e

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: ./release.sh <version> <port>"
  exit 1
fi

VERSION=$1
PORT=$2
COMPOSE_FILE=docker-compose.yml
BACKUP_FILE=docker-compose.yml.bak

echo "[1/6] Setting Maven version to $VERSION"
mvn versions:set -DnewVersion="$VERSION" -DgenerateBackupPoms=false

echo "[2/6] Building Docker image"
docker build \
  --build-arg APP_VERSION="$VERSION" \
  --build-arg APP_PORT="$PORT" \
  -t ads-backend:"$VERSION" .

echo "[3/6] Backing up and updating docker-compose.yml"
cp $COMPOSE_FILE $BACKUP_FILE

# Use sed to replace lines
sed -i "s|image: ads-backend:.*|image: ads-backend:$VERSION|" $COMPOSE_FILE
sed -i "s|APP_VERSION: .*|APP_VERSION: $VERSION|" $COMPOSE_FILE

echo "[4/6] Committing version changes and tagging in Git"
git add pom.xml $COMPOSE_FILE
git commit -m "Release v$VERSION"
git tag v"$VERSION"
git push origin main
git push origin v"$VERSION"

echo "[5/6] Running docker-compose with updated image"
docker-compose up --build

echo "[6/6] Done. Your app is running on port $PORT"
