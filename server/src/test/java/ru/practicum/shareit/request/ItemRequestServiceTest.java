package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        ItemRequestDto request = new ItemRequestDto();
        request.setDescription("need item");

        ItemRequestDto result = itemRequestService.createRequest(request, user.getId());

        assertNotNull(result.getId());
        assertEquals("need item", result.getDescription());
    }

    @Test
    void getUserRequests() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        ItemRequestDto request = new ItemRequestDto();
        request.setDescription("request");
        itemRequestService.createRequest(request, user.getId());

        List<ItemRequestDto> result = itemRequestService.getUserRequests(user.getId());

        assertEquals(1, result.size());
    }

    @Test
    void getOtherUsersRequests() {
        User user1 = userRepository.save(new User(null, "user1", "user1@email.com"));
        User user2 = userRepository.save(new User(null, "user2", "user2@email.com"));
        ItemRequestDto request = new ItemRequestDto();
        request.setDescription("request");
        itemRequestService.createRequest(request, user1.getId());

        List<ItemRequestDto> result = itemRequestService.getOtherUsersRequests(user2.getId(), 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getById() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        ItemRequestDto request = new ItemRequestDto();
        request.setDescription("request");
        ItemRequestDto created = itemRequestService.createRequest(request, user.getId());

        ItemRequestDto result = itemRequestService.getRequestById(created.getId(), user.getId());

        assertEquals(created.getId(), result.getId());
    }
}
