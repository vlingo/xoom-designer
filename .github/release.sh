#!/bin/sh
VERSION=$1

[[ "$VERSION" != "" ]] || (echo "The version has to be passed as the first argument" && exit)

SCRIPT_PATH=$(cd -- "$(dirname "$0")/" >/dev/null 2>&1 ; pwd -P)

$SCRIPT_PATH/update-version.sh $VERSION

sed -i'.bkp' -e '/<artifactId>xoom-[a-z\-]*<\/artifactId>/{' -e 'n;s/\(<version>\)[0-9\.]*\(<\/version>\)/\1'$VERSION'\2/' -e '}' -e 's/\(io.vlingo.xoom:xoom-[a-z\-]*\):[0-9\.]*/\1:'$VERSION'/' README.md
sed -i'.bkp' -E 's/(xoom-designer-)[1-9]*\.[0-9]*\.[0-9]*\.jar/\1'$VERSION'.jar/g' README.md
sed -i'.bkp' -E 's/(image: vlingo\/xoom-designer:)[1-9]*\.[0-9]*\.[0-9]*/\1'$VERSION'/g' docker-compose.yml
sed -i'.bkp' -E 's/(image: vlingo\/xoom-schemata:)[1-9]*\.[0-9]*\.[0-9]*/\1'$VERSION'/g' docker-compose.yml

git add README.md docker-compose.yml
git commit -m "Release v$VERSION"
git tag "$VERSION" -m "Release v$VERSION"
git push --follow-tags

