package com.velocitymotors.booking.api;

import com.velocitymotors.booking.model.Booking;
import com.velocitymotors.booking.model.BookingStatus;

public record BookingResponse(String bookingId, BookingStatus bookingStatus) {

    public static BookingResponse from(Booking booking) {
        return new BookingResponse(booking.getBookingId(), booking.getStatus());
    }

}
