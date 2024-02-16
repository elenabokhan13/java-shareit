package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.*;

@Component
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        id += 1;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public User updateUser(User user) {
        User currentUser = getUserById(user.getId());
        if (user.getName() != null) {
            currentUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            currentUser.setEmail(user.getEmail());
        }
        users.remove(user.getId());
        users.put(currentUser.getId(), currentUser);
        return currentUser;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }
}
