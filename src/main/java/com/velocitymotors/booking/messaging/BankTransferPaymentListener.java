package com.velocitymotors.booking.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velocitymotors.booking.service.BookingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPaymentListener {
    private final ObjectMapper objectMapper;
    private final BookingService bookingService;

    public BankTransferPaymentListener(ObjectMapper objectMapper, BookingService bookingService) {
        this.objectMapper = objectMapper;
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "bank-transfer-payment-events")
    public void receive(String message) throws Exception {
        BankTransferPaymentEvent event = objectMapper.readValue(message, BankTransferPaymentEvent.class);
        if (event.transactionDetails() == null) {
            return;
        }

        // The booking id is the second value in: <transaction reference> <booking id>.
        String[] values = event.transactionDetails().trim().split("\\s+");
        if (values.length >= 2) {
            bookingService.confirmBankTransfer(values[1]);
        }
    }
}
