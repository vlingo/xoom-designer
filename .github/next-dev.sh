#!/bin/sh
VERSION=$1

[[ "$VERSION" != "" ]] || (echo "The version has to be passed as the first argument" && exit)

SCRIPT_PATH=$(cd -- "$(dirname "$0")/" >/dev/null 2>&1 ; pwd -P)

$SCRIPT_PATH/update-version.sh $VERSION

git commit -m "Next development version $3"
git push --follow-tags

