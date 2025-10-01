package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    @NotNull(message = "Item ID cannot be null")
    private Long itemId;

    @FutureOrPresent(message = "Start date cannot be in the past")
    @NotNull(message = "Start date cannot be null")
    private LocalDateTime start;

    @Future(message = "End date must be in the future")
    @NotNull(message = "End date cannot be null")
    private LocalDateTime end;
}