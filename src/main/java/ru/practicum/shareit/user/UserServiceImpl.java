package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long nextId = 1L;

    @Override
    public User createUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setId(nextId++);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = users.get(userId);
        if (existingUser == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (emails.contains(user.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
            emails.remove(existingUser.getEmail());
            emails.add(user.getEmail());
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
            throw new NotFoundException("User with id " + userId + " not found");
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
            throw new NotFoundException("User with id " + userId + " not found");
        }
        emails.remove(user.getEmail());
        users.remove(userId);
    }
}