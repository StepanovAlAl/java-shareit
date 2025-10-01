package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Test
    void getUserItems() {
        UserDto userDto = new UserDto(null, "user", "user@email.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto request = new ItemRequestDto("item", "desc", true, null);
        itemService.createItem(request, createdUser.getId());

        var result = itemService.getUserItems(createdUser.getId());

        assertEquals(1, result.size());
        assertEquals("item", result.get(0).getName());
    }

    @Test
    void searchItems() {
        UserDto userDto = new UserDto(null, "user", "user@email.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto request = new ItemRequestDto("drill", "tool", true, null);
        itemService.createItem(request, createdUser.getId());

        var result = itemService.searchItems("drill");

        assertEquals(1, result.size());
        assertEquals("drill", result.get(0).getName());
    }

    @Test
    void searchEmpty() {
        var result = itemService.searchItems("");

        assertTrue(result.isEmpty());
    }

    @Test
    void createItem() {
        UserDto userDto = new UserDto(null, "user", "user@email.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto request = new ItemRequestDto("item", "desc", true, null);

        ItemDto result = itemService.createItem(request, createdUser.getId());

        assertNotNull(result.getId());
        assertEquals("item", result.getName());
    }

    @Test
    void getItem() {
        UserDto userDto = new UserDto(null, "user", "user@email.com");
        UserDto createdUser = userService.createUser(userDto);

        ItemRequestDto request = new ItemRequestDto("item", "desc", true, null);
        ItemDto item = itemService.createItem(request, createdUser.getId());

        var result = itemService.getItemDtoById(item.getId(), createdUser.getId());

        assertEquals(item.getId(), result.getId());
    }
}
