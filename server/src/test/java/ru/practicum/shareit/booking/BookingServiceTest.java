package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Test
    void create() {
        User owner = userRepository.save(new User(null, "owner", "owner@email.com"));
        User booker = userRepository.save(new User(null, "booker", "booker@email.com"));
        Item item = itemRepository.save(new Item(null, "item", "desc", true, owner, null));

        BookingRequestDto request = new BookingRequestDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        BookingResponseDto result = bookingService.createBooking(request, booker.getId());

        assertNotNull(result.getId());
        assertEquals(booker.getId(), result.getBooker().getId());
    }

    @Test
    void getUserBookings() {
        User owner = userRepository.save(new User(null, "owner", "owner@email.com"));
        User booker = userRepository.save(new User(null, "booker", "booker@email.com"));
        Item item = itemRepository.save(new Item(null, "item", "desc", true, owner, null));

        BookingRequestDto request = new BookingRequestDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        bookingService.createBooking(request, booker.getId());

        List<BookingResponseDto> result = bookingService.getUserBookings(booker.getId(), "ALL", 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getOwnerBookings() {
        User owner = userRepository.save(new User(null, "owner", "owner@email.com"));
        User booker = userRepository.save(new User(null, "booker", "booker@email.com"));
        Item item = itemRepository.save(new Item(null, "item", "desc", true, owner, null));

        BookingRequestDto request = new BookingRequestDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        bookingService.createBooking(request, booker.getId());

        List<BookingResponseDto> result = bookingService.getOwnerBookings(owner.getId(), "ALL", 0, 10);

        assertEquals(1, result.size());
    }
}