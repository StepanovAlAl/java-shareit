package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUserItems() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        ItemRequestDto request = new ItemRequestDto("item", "desc", true, null);
        itemService.createItem(request, user.getId());

        var result = itemService.getUserItems(user.getId());

        assertEquals(1, result.size());
        assertEquals("item", result.get(0).getName());
    }

    @Test
    void searchItems() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        ItemRequestDto request = new ItemRequestDto("drill", "tool", true, null);
        itemService.createItem(request, user.getId());

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
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        ItemRequestDto request = new ItemRequestDto("item", "desc", true, null);

        ItemDto result = itemService.createItem(request, user.getId());

        assertNotNull(result.getId());
        assertEquals("item", result.getName());
    }

    @Test
    void getItem() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        ItemRequestDto request = new ItemRequestDto("item", "desc", true, null);
        ItemDto item = itemService.createItem(request, user.getId());

        var result = itemService.getItemDtoById(item.getId(), user.getId());

        assertEquals(item.getId(), result.getId());
    }
}
