#!/bin/sh

VERSION="$1"

[ "$VERSION" != "" ] || (echo "The version has to be passed as the first argument" && exit)

mvn versions:set -DnewVersion=$VERSION -DgenerateBackupPoms=false
mvn versions:use-dep-version -Dincludes=io.vlingo.xoom -DdepVersion=$VERSION -DforceVersion=true -DgenerateBackupPoms=false

git add pom.xml
