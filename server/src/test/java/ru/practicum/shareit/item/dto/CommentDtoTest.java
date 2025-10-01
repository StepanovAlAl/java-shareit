package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentResponseDto> json;

    @Test
    void serialize() throws IOException {
        CommentResponseDto dto = new CommentResponseDto(1L, "text", "author", LocalDateTime.now());

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("author");
    }

    @Test
    void deserialize() throws IOException {
        String content = "{\"id\":1,\"text\":\"text\",\"authorName\":\"author\",\"created\":\"2024-01-01T10:00:00\"}";

        CommentResponseDto dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("text");
        assertThat(dto.getAuthorName()).isEqualTo("author");
    }
}