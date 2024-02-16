package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user);

    void deleteUser(Long userId);

    User updateUser(User user);

    Collection<User> getUsers();

    User getUserById(Long id);

}
