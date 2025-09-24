package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long userId, User userUpdates) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        if (userUpdates.getEmail() != null && !userUpdates.getEmail().equals(existingUser.getEmail())) {

            Optional<User> userWithNewEmail = userRepository.findByEmail(userUpdates.getEmail());
            if (userWithNewEmail.isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
            existingUser.setEmail(userUpdates.getEmail());
        }

        if (userUpdates.getName() != null) {
            existingUser.setName(userUpdates.getName());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }
}