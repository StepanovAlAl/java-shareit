package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ItemClientTest {

    @Autowired
    private ItemClient itemClient;

    @Test
    void createItem() {
        // Тест для клиента Gateway
    }
}