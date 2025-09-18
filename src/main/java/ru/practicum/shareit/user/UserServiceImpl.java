package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> emailToUserId = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public User createUser(User user) {
        if (emailToUserId.containsKey(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setId(nextId++);
        users.put(user.getId(), user);
        emailToUserId.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = users.get(userId);
        if (existingUser == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (emailToUserId.containsKey(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            emailToUserId.remove(existingUser.getEmail());
            emailToUserId.put(user.getEmail(), userId);
            existingUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        return existingUser;
    }

    @Override
    public User getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        emailToUserId.remove(user.getEmail());
        users.remove(userId);
    }
}