package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void create() throws Exception {
        BookingRequestDto request = new BookingRequestDto(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        BookingResponseDto response = new BookingResponseDto(1L,
                request.getStart(), request.getEnd(),
                ru.practicum.shareit.booking.BookingStatus.WAITING,
                new BookingResponseDto.BookerDto(1L, "user"),
                new BookingResponseDto.ItemDto(1L, "item"));

        when(bookingService.createBooking(any(), anyLong())).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateStatus() throws Exception {
        BookingResponseDto response = new BookingResponseDto(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                ru.practicum.shareit.booking.BookingStatus.APPROVED,
                new BookingResponseDto.BookerDto(1L, "user"),
                new BookingResponseDto.ItemDto(1L, "item"));

        when(bookingService.updateBookingStatus(anyLong(), anyBoolean(), anyLong())).thenReturn(response);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBooking() throws Exception {
        BookingResponseDto response = new BookingResponseDto(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                ru.practicum.shareit.booking.BookingStatus.WAITING,
                new BookingResponseDto.BookerDto(1L, "user"),
                new BookingResponseDto.ItemDto(1L, "item"));

        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserBookings() throws Exception {
        BookingResponseDto booking = new BookingResponseDto(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                ru.practicum.shareit.booking.BookingStatus.WAITING,
                new BookingResponseDto.BookerDto(1L, "user"),
                new BookingResponseDto.ItemDto(1L, "item"));

        when(bookingService.getUserBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getOwnerBookings() throws Exception {
        BookingResponseDto booking = new BookingResponseDto(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                ru.practicum.shareit.booking.BookingStatus.WAITING,
                new BookingResponseDto.BookerDto(1L, "user"),
                new BookingResponseDto.ItemDto(1L, "item"));

        when(bookingService.getOwnerBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}