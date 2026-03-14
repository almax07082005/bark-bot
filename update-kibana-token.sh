#!/usr/bin/env bash

set -euo pipefail

# Always run from repo root (script location)
cd "$(dirname "$0")"

ENV_FILE=".env"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "Error: $ENV_FILE not found in $(pwd)" >&2
  exit 1
fi

echo "Creating Kibana service account token in Elasticsearch..."
RAW_OUTPUT="$(docker compose exec -T elasticsearch bin/elasticsearch-service-tokens create elastic/kibana bark-bot-kibana)"
TOKEN="$(printf '%s\n' "$RAW_OUTPUT" | awk -F' = ' '/SERVICE_TOKEN/{print $2}')"

if [[ -z "${TOKEN:-}" ]]; then
  echo "Error: failed to parse token from elasticsearch-service-tokens output:" >&2
  printf '%s\n' "$RAW_OUTPUT" >&2
  exit 1
fi

echo "Updating KIBANA_SERVICE_ACCOUNT_TOKEN in $ENV_FILE..."
tmp_env="$(mktemp)"
grep -v '^KIBANA_SERVICE_ACCOUNT_TOKEN=' "$ENV_FILE" > "$tmp_env" || true
echo "KIBANA_SERVICE_ACCOUNT_TOKEN=$TOKEN" >> "$tmp_env"
mv "$tmp_env" "$ENV_FILE"

echo "Restarting Kibana container..."
docker compose up -d && docker compose restart kibana

echo "Done. Kibana now uses the new service account token."
