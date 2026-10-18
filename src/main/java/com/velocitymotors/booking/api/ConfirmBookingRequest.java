package com.velocitymotors.booking.api;

import com.velocitymotors.booking.model.PaymentMode;
import com.velocitymotors.booking.model.VehicleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ConfirmBookingRequest(
        @NotBlank String customerName,
        @NotBlank String vehicleId,
        @NotNull LocalDate rentalStartDate,
        @NotNull LocalDate rentalEndDate,
        @NotNull VehicleCategory vehicleCategory,
        @NotNull PaymentMode paymentMode,
        String paymentReference) {
}
