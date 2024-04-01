package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту /users для получения пользователя по id {}", userId);
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Получен запрос к эндпойнту /users для получения списка пользователя");
        return userClient.getUsers();
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Получен запрос к эндпойнту /users для создания пользователя");
        return userClient.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту /users для обновления пользователя по id {}", userId);
        return userClient.updateUser(userDto, userId);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту /users для удаления пользователя по id {}", userId);
        return userClient.deleteUser(userId);
    }
}
