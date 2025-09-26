package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId);

    BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingResponseDto getBookingById(Long bookingId, Long userId);

    List<BookingResponseDto> getUserBookings(Long userId, String state, int from, int size);

    List<BookingResponseDto> getOwnerBookings(Long userId, String state, int from, int size);
}