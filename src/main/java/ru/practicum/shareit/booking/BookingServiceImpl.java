package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ItemAccessDeniedException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId) {
        User booker = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingRequestDto.getItemId());

        if (!item.getAvailable()) {
            throw new ItemValidationException("Item is not available for booking");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Owner cannot book their own item");
        }

        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart()) ||
                bookingRequestDto.getEnd().equals(bookingRequestDto.getStart())) {
            throw new ItemValidationException("End date must be after start date");
        }


        if (hasOverlappingBookings(item.getId(), bookingRequestDto.getStart(), bookingRequestDto.getEnd())) {
            throw new ItemValidationException("Booking time overlaps with existing booking");
        }

        Booking booking = BookingMapper.toBooking(bookingRequestDto, item, booker);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        // Проверяем существование пользователя - если пользователь не существует, будет 404
        User user = userService.getUserById(userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        // Теперь проверяем, является ли пользователь владельцем вещи
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ItemAccessDeniedException("Only owner can update booking status");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ItemValidationException("Booking status cannot be changed");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(updatedBooking);
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {

        User user = userService.getUserById(userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Booking with id " + bookingId + " not found");
        }

        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getUserBookings(Long userId, String state, int from, int size) {

        User user = userService.getUserById(userId);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        BookingState bookingState = parseState(state);

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId, pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, now, now, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, now, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, now, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.REJECTED, pageable);
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }

        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Long userId, String state, int from, int size) {

        User user = userService.getUserById(userId);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        BookingState bookingState = parseState(state);

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, now, now, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.REJECTED, pageable);
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }

        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    private BookingState parseState(String state) {
        if (state == null) {
            return BookingState.ALL;
        }
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    private boolean hasOverlappingBookings(Long itemId, LocalDateTime start, LocalDateTime end) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                itemId, start, end, BookingStatus.APPROVED);
        return !overlappingBookings.isEmpty();
    }
}