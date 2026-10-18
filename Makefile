IMAGE_NAME := car-booking-service
CONTAINER_NAME := car-booking-service
PORT := 8080

.PHONY: build start stop restart unit-test e2e test

build:
	docker build -t $(IMAGE_NAME) .

start: build
	-docker rm -f $(CONTAINER_NAME)
	docker run -d \
		--name $(CONTAINER_NAME) \
		-p $(PORT):8080 \
		-e CREDIT_CARD_SERVICE_URL=$${CREDIT_CARD_SERVICE_URL:-http://host.docker.internal:9090/host/credit-card-payment-api} \
		-e KAFKA_BOOTSTRAP_SERVERS=$${KAFKA_BOOTSTRAP_SERVERS:-host.docker.internal:9092} \
		$(IMAGE_NAME)

stop:
	-docker stop $(CONTAINER_NAME)

restart: stop start

unit-test:
	./gradlew test

e2e: start
	@trap '$(MAKE) stop' EXIT; sh scripts/e2e-test.sh

test: unit-test e2e
