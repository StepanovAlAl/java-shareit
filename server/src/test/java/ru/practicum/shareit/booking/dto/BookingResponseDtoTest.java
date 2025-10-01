package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingResponseDtoTest {

    @Autowired
    private JacksonTester<BookingResponseDto> json;

    @Test
    void serialize() throws IOException {
        BookingResponseDto.BookerDto booker = new BookingResponseDto.BookerDto(1L, "user");
        BookingResponseDto.ItemDto item = new BookingResponseDto.ItemDto(1L, "item");
        BookingResponseDto dto = new BookingResponseDto(1L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 10, 0),
                ru.practicum.shareit.booking.BookingStatus.APPROVED,
                booker, item);

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
    }

    @Test
    void deserialize() throws IOException {
        String content = "{\"id\":1,\"start\":\"2024-01-01T10:00:00\",\"end\":\"2024-01-02T10:00:00\",\"status\":\"APPROVED\",\"booker\":{\"id\":1,\"name\":\"user\"},\"item\":{\"id\":1,\"name\":\"item\"}}";

        BookingResponseDto dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(ru.practicum.shareit.booking.BookingStatus.APPROVED);
        assertThat(dto.getBooker().getId()).isEqualTo(1L);
        assertThat(dto.getItem().getId()).isEqualTo(1L);
    }
}