package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ServerErrorException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.validator.Validator;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            User user = userRepository.save(userMapper.dtoToUser(userDto));
            deleteUser(user.getId());
            throw new ServerErrorException("Пользователь с таким имейл уже существует.");
        }
        User user = userRepository.save(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        userDto.setId(userId);
        User currentUser = Validator.validateUserAndReturn(userRepository, userId);
        if (userDto.getName() != null) {
            currentUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().equals(currentUser.getEmail())) {
                if (userRepository.findByEmail(userDto.getEmail()) != null) {
                    throw new ServerErrorException("Пользователь с таким имейл уже существует.");
                } else {
                    currentUser.setEmail(userDto.getEmail());
                }
            }
        }
        User user = userRepository.save(currentUser);
        return userMapper.userToDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = Validator.validateUserAndReturn(userRepository, id);
        return userMapper.userToDto(user);
    }
}
