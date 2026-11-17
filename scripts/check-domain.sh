#!/bin/sh
set -eu
taller_root=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
cd "$taller_root"
taller_check_dir=$(mktemp -d)
trap 'rm -rf "$taller_check_dir"' EXIT HUP INT TERM
find question-service/src/main/java/co/edu/unicauca/saberpro/question/domain \
     question-service/src/main/java/co/edu/unicauca/saberpro/question/application \
     -name '*.java' -print > "$taller_check_dir/sources"
javac --release 17 -d "$taller_check_dir/classes" @"$taller_check_dir/sources"
printf 'Dominio y aplicación compilados con Java puro, sin classpath externo.\n'
