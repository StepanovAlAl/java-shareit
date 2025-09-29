package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void create() throws Exception {
        ItemRequestDto request = new ItemRequestDto("item", "desc", true, null);
        ItemDto response = new ItemDto(1L, "item", "desc", true, 1L, null);

        when(itemService.createItem(any(), anyLong())).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("item"));
    }

    @Test
    void getItem() throws Exception {
        ItemResponseDto response = new ItemResponseDto(1L, "item", "desc", true, null, null, List.of());

        when(itemService.getItemDtoById(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserItems() throws Exception {
        ItemWithBookingsDto item = new ItemWithBookingsDto(1L, "item", "desc", true, null, null, List.of());

        when(itemService.getUserItems(anyLong())).thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void search() throws Exception {
        ItemDto item = new ItemDto(1L, "drill", "tool", true, 1L, null);

        when(itemService.searchItems(anyString())).thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "drill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("drill"));
    }

    @Test
    void update() throws Exception {
        ItemRequestDto request = new ItemRequestDto("updated", "desc", true, null);
        ItemDto response = new ItemDto(1L, "updated", "desc", true, 1L, null);

        when(itemService.updateItem(anyLong(), any(), anyLong())).thenReturn(response);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated"));
    }
}
