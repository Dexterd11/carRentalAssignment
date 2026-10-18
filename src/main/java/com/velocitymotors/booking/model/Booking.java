package com.velocitymotors.booking.model;

import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private String customerName;
    private String vehicleId;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private VehicleCategory vehicleCategory;
    private PaymentMode paymentMode;
    private String paymentReference;
    private BookingStatus status;

    public Booking(String bookingId, String customerName, String vehicleId,
                   LocalDate rentalStartDate, LocalDate rentalEndDate,
                   VehicleCategory vehicleCategory, PaymentMode paymentMode,
                   String paymentReference, BookingStatus status) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.vehicleId = vehicleId;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.vehicleCategory = vehicleCategory;
        this.paymentMode = paymentMode;
        this.paymentReference = paymentReference;
        this.status = status;
    }

    public String getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public String getVehicleId() { return vehicleId; }
    public LocalDate getRentalStartDate() { return rentalStartDate; }
    public LocalDate getRentalEndDate() { return rentalEndDate; }
    public VehicleCategory getVehicleCategory() { return vehicleCategory; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public String getPaymentReference() { return paymentReference; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
