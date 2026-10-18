# Car Booking Service

Simple Java 17 Spring Boot implementation of the car rental assignment.

## Run With Gradle

```bash
./gradlew bootRun
```

The service listens on port `8080`. The credit-card service URL can be changed with
`CREDIT_CARD_SERVICE_URL`. Kafka is expected on `localhost:9092`, or the broker can
be changed with `KAFKA_BOOTSTRAP_SERVERS`.

## Confirm a booking

`POST /api/bookings`

```json
{
  "customerName": "Alex Smith",
  "vehicleId": "CAR-1001",
  "rentalStartDate": "2026-11-01",
  "rentalEndDate": "2026-11-05",
  "vehicleCategory": "SUV",
  "paymentMode": "BANK_TRANSFER",
  "paymentReference": "TRANSFER-123"
}
```

The service uses an in-memory map instead of a database to keep the assignment easy
to understand. Vehicle validation is mocked by requiring a non-empty vehicle ID.
Since the request has no booking amount, a valid bank-transfer event is treated as
full payment. The booking ID is the second value in `transactionDetails`, for
example `TXN987654321 BKG0012345`.

## Run With Docker

```bash
make start
make stop
make restart
make test
```

`make test` runs the Gradle tests and then starts the Docker container to test a
real booking through HTTP. The container is stopped automatically after the test.

The container exposes port `8080`. Override the external service locations when
needed:

```bash
CREDIT_CARD_SERVICE_URL=http://credit-card-service:9090/host/credit-card-payment-api \
KAFKA_BOOTSTRAP_SERVERS=kafka:9092 make start
```
