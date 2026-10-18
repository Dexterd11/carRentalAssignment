package com.velocitymotors.booking.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CreditCardClient {
    private final RestClient client;

    public CreditCardClient(RestClient.Builder builder,
                            @Value("${credit-card-service.base-url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public boolean isApproved(String paymentReference) {
        PaymentStatusResponse response = client.post()
                .uri("/payment-status")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PaymentStatusRequest(paymentReference))
                .retrieve()
                .body(PaymentStatusResponse.class);
        return response != null && "APPROVED".equalsIgnoreCase(response.status());
    }
}
