package com.velocitymotors.booking.service;

import com.velocitymotors.booking.api.ConfirmBookingRequest;
import com.velocitymotors.booking.integration.CreditCardClient;
import com.velocitymotors.booking.model.BookingStatus;
import com.velocitymotors.booking.model.PaymentMode;
import com.velocitymotors.booking.model.VehicleCategory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {
    private final CreditCardClient creditCardClient = mock(CreditCardClient.class);
    private final BookingService bookingService = new BookingService(creditCardClient);

    @Test
    void approvedCreditCardPaymentConfirmsBooking() {
        when(creditCardClient.isApproved("CARD-123")).thenReturn(true);

        var booking = bookingService.createBooking(creditCardRequest());

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void rejectedCreditCardPaymentFailsBooking() {
        when(creditCardClient.isApproved("CARD-123")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(creditCardRequest()));
    }

    private ConfirmBookingRequest creditCardRequest() {
        return new ConfirmBookingRequest(
                "Test Customer", "CAR-001", LocalDate.of(2030, 11, 1),
                LocalDate.of(2030, 11, 5), VehicleCategory.SUV,
                PaymentMode.CREDIT_CARD, "CARD-123");
    }
}
