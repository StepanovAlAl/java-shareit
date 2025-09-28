package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {
    public static Booking toBooking(BookingRequestDto bookingRequestDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(ru.practicum.shareit.booking.BookingStatus.WAITING);
        return booking;
    }

    public static BookingResponseDto toDto(Booking booking) {
        BookingResponseDto.BookerDto bookerDto = new BookingResponseDto.BookerDto(
                booking.getBooker().getId(), booking.getBooker().getName());

        BookingResponseDto.ItemDto itemDto = new BookingResponseDto.ItemDto(
                booking.getItem().getId(), booking.getItem().getName());

        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                bookerDto,
                itemDto
        );
    }
}