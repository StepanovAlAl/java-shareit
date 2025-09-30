package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = toUser(userDto);

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userUpdates) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        if (userUpdates.getEmail() != null && !userUpdates.getEmail().isBlank()) {
            if (!userUpdates.getEmail().equals(existingUser.getEmail())) {
                Optional<User> userWithNewEmail = userRepository.findByEmail(userUpdates.getEmail());
                if (userWithNewEmail.isPresent()) {
                    throw new EmailAlreadyExistsException("Email already exists");
                }
                existingUser.setEmail(userUpdates.getEmail());
            }
        }

        if (userUpdates.getName() != null && !userUpdates.getName().isBlank()) {
            existingUser.setName(userUpdates.getName());
        }

        User updatedUser = userRepository.save(existingUser);
        return toDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        return toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }

    private User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}