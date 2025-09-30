package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create() {
        UserDto userDto = new UserDto(null, "user", "user@email.com");

        UserDto result = userService.createUser(userDto);

        assertNotNull(result.getId());
        assertEquals("user", result.getName());
    }

    @Test
    void update() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        UserDto updates = new UserDto(null, "new", "new@email.com");

        UserDto result = userService.updateUser(user.getId(), updates);

        assertEquals("new", result.getName());
        assertEquals("new@email.com", result.getEmail());
    }

    @Test
    void get() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));

        UserDto result = userService.getUserById(user.getId());

        assertEquals(user.getId(), result.getId());
    }

    @Test
    void getAll() {
        userRepository.save(new User(null, "user1", "user1@email.com"));
        userRepository.save(new User(null, "user2", "user2@email.com"));

        List<UserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void delete() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));

        userService.deleteUser(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }
}
