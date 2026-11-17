#!/bin/sh
set -eu
taller_root=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
cd "$taller_root"
docker compose config --quiet
docker compose up -d --build --wait
./mvnw -B -ntp -Pintegration verify
python3 scripts/smoke_test.py --label "${APP_REPOSITORY:-jpa}" "$@"
