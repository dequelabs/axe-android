#!/usr/bin/env bash

# Merge develop and master
${git} checkout master
${git} fetch --tags
${git} reset --hard origin/master
${git} merge --ff-only develop
${git} push
