package com.velocitymotors.booking.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingCancellationJob {
    private final BookingService bookingService;

    public BookingCancellationJob(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cancelOverdueBookings() {
        bookingService.cancelUnpaidBookings();
    }
}
