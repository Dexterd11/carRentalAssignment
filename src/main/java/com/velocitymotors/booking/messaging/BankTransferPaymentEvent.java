package com.velocitymotors.booking.messaging;

public record BankTransferPaymentEvent(
        String paymentId,
        String senderAccountNumber,
        double paymentAmount,
        String transactionDetails) {
}
