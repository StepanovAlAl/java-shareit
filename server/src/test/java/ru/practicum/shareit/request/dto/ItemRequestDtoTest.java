package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void serialize() throws IOException {
        ItemRequestDto.ItemResponse itemResponse = new ItemRequestDto.ItemResponse(1L, "item", 1L);
        ItemRequestDto dto = new ItemRequestDto(1L, "need item", LocalDateTime.now(), List.of(itemResponse));

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("need item");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
    }

    @Test
    void deserialize() throws IOException {
        String content = "{\"id\":1,\"description\":\"need item\",\"created\":\"2024-01-01T10:00:00\",\"items\":[{\"id\":1,\"name\":\"item\",\"ownerId\":1}]}";

        ItemRequestDto dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("need item");
        assertThat(dto.getItems()).hasSize(1);
    }
}