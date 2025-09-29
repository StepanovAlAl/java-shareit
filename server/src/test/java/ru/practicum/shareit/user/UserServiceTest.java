package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create() {
        User user = new User(null, "user", "user@email.com");

        User result = userService.createUser(user);

        assertNotNull(result.getId());
        assertEquals("user", result.getName());
    }

    @Test
    void update() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));
        User updates = new User(null, "new", "new@email.com");

        User result = userService.updateUser(user.getId(), updates);

        assertEquals("new", result.getName());
        assertEquals("new@email.com", result.getEmail());
    }

    @Test
    void get() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));

        User result = userService.getUserById(user.getId());

        assertEquals(user.getId(), result.getId());
    }

    @Test
    void getAll() {
        userRepository.save(new User(null, "user1", "user1@email.com"));
        userRepository.save(new User(null, "user2", "user2@email.com"));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void delete() {
        User user = userRepository.save(new User(null, "user", "user@email.com"));

        userService.deleteUser(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }
}