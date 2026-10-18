package com.velocitymotors.booking.service;

import com.velocitymotors.booking.api.ConfirmBookingRequest;
import com.velocitymotors.booking.integration.CreditCardClient;
import com.velocitymotors.booking.model.Booking;
import com.velocitymotors.booking.model.BookingStatus;
import com.velocitymotors.booking.model.PaymentMode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookingService {
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final CreditCardClient creditCardClient;

    public BookingService(CreditCardClient creditCardClient) {
        this.creditCardClient = creditCardClient;
    }

    public Booking createBooking(ConfirmBookingRequest request) {
        validateDates(request.rentalStartDate(), request.rentalEndDate());

        if (request.paymentMode() == PaymentMode.CREDIT_CARD &&
                (request.paymentReference() == null || request.paymentReference().isBlank())) {
            throw new IllegalArgumentException("Payment reference is required for credit card payments");
        }

        BookingStatus status;
        if (request.paymentMode() == PaymentMode.BANK_TRANSFER) {
            status = BookingStatus.PENDING_PAYMENT;
        } else if (request.paymentMode() == PaymentMode.CREDIT_CARD) {
            if (!creditCardClient.isApproved(request.paymentReference())) {
                throw new IllegalArgumentException("Credit card payment was not approved");
            }
            status = BookingStatus.CONFIRMED;
        } else {
            status = BookingStatus.CONFIRMED;
        }

        Booking booking = new Booking(
                newBookingId(), request.customerName(), request.vehicleId(),
                request.rentalStartDate(), request.rentalEndDate(),
                request.vehicleCategory(), request.paymentMode(),
                request.paymentReference(), status);
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }

    public void confirmBankTransfer(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null && booking.getStatus() == BookingStatus.PENDING_PAYMENT) {
            booking.setStatus(BookingStatus.CONFIRMED);
        }
    }

    public void cancelUnpaidBookings() {
        LocalDate cutoff = LocalDate.now().plusDays(2);
        for (Booking booking : bookings.values()) {
            if (booking.getStatus() == BookingStatus.PENDING_PAYMENT &&
                    !booking.getRentalStartDate().isAfter(cutoff)) {
                booking.setStatus(BookingStatus.CANCELLED);
            }
        }
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Rental end date must be after rental start date");
        }
        if (start.plusDays(21).isBefore(end)) {
            throw new IllegalArgumentException("A vehicle cannot be booked for more than 21 days");
        }
    }

    private String newBookingId() {
        return "BKG" + UUID.randomUUID().toString().replace("-", "").substring(0, 7).toUpperCase();
    }
}
