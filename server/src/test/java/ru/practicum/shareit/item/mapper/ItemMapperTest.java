package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void toItem() {
        ItemRequestDto dto = new ItemRequestDto("item", "desc", true, 1L);
        User owner = new User(1L, "owner", "owner@email.com");

        Item item = ItemMapper.toItem(dto, owner);

        assertEquals("item", item.getName());
        assertEquals("desc", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
    }

    @Test
    void toDto() {
        User owner = new User(1L, "owner", "owner@email.com");
        Item item = new Item(1L, "item", "desc", true, owner, null);

        var dto = ItemMapper.toDto(item);

        assertEquals(1L, dto.getId());
        assertEquals("item", dto.getName());
        assertEquals("desc", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertEquals(1L, dto.getOwnerId());
    }
}