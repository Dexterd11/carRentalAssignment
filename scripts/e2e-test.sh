#!/bin/sh

set -eu

BASE_URL="${BASE_URL:-http://localhost:8080}"
REQUEST='{
  "customerName": "E2E Customer",
  "vehicleId": "E2E-CAR-001",
  "rentalStartDate": "2030-11-01",
  "rentalEndDate": "2030-11-05",
  "vehicleCategory": "SUV",
  "paymentMode": "CASH"
}'

response_file="$(mktemp)"
trap 'rm -f "$response_file"' EXIT

printf 'Waiting for the application at %s...\n' "$BASE_URL"
attempt=1
while [ "$attempt" -le 30 ]; do
    status="$(curl -s -o "$response_file" -w '%{http_code}' \
        -X POST "$BASE_URL/api/bookings" \
        -H 'Content-Type: application/json' \
        -d "$REQUEST" || true)"

    if [ "$status" = "201" ]; then
        break
    fi

    sleep 2
    attempt=$((attempt + 1))
done

if [ "${status:-}" != "201" ]; then
    printf 'E2E test failed: expected HTTP 201, received %s\n' "${status:-no response}"
    cat "$response_file"
    exit 1
fi

if ! grep -q '"bookingStatus":"CONFIRMED"' "$response_file"; then
    printf 'E2E test failed: booking was not confirmed\n'
    cat "$response_file"
    exit 1
fi

printf 'E2E test passed: cash booking was confirmed\n'
cat "$response_file"
