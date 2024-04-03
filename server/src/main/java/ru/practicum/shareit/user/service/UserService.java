package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    void deleteUser(Long userId);

    Collection<UserDto> getUsers();

    UserDto getUserById(Long id);
}
