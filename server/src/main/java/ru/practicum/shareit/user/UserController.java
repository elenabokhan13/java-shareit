package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;


@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту /users для получения пользователя по id {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Получен запрос к эндпойнту /users для получения списка пользователя");
        return userService.getUsers();
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Получен запрос к эндпойнту /users для создания пользователя");
        return userService.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту /users для обновления пользователя по id {}", userId);
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту /users для удаления пользователя по id {}", userId);
        userService.deleteUser(userId);
    }
}
