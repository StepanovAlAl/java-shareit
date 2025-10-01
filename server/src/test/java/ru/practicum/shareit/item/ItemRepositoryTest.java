package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByOwnerId() {
        User owner = userRepository.save(new User(null, "owner", "owner@email.com"));
        itemRepository.save(new Item(null, "item1", "desc1", true, owner, null));
        itemRepository.save(new Item(null, "item2", "desc2", true, owner, null));

        List<Item> items = itemRepository.findByOwnerIdOrderById(owner.getId());

        assertEquals(2, items.size());
    }

    @Test
    void searchAvailableItems() {
        User owner = userRepository.save(new User(null, "owner", "owner@email.com"));
        itemRepository.save(new Item(null, "drill", "power tool", true, owner, null));

        List<Item> items = itemRepository.searchAvailableItems("drill");

        assertEquals(1, items.size());
        assertEquals("drill", items.get(0).getName());
    }
}