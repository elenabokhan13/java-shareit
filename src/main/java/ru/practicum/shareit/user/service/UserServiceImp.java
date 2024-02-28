package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class UserServiceImp implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImp(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userStorage.createUser(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        userDto.setId(userId);
        User user = userStorage.updateUser(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(userMapper::userToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.userToDto(userStorage.getUserById(id));
    }
}
