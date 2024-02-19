package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Component
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new InvalidRequestException("Пользователь с таким имейл уже существует.");
        }
        id += 1;
        emails.add(user.getEmail());
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        emails.remove(user.getEmail());
        users.remove(userId);
    }

    @Override
    public User updateUser(User user) {
        User currentUser = users.get(user.getId());
        if (currentUser == null) {
            throw new InvalidRequestException("Данный пользователь еще не создан");
        }
        if (user.getName() != null) {
            currentUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(currentUser.getEmail())) {
                if (emails.contains(user.getEmail())) {
                    throw new InvalidRequestException("Пользователь с таким имейл уже существует.");
                } else {
                    emails.remove(currentUser.getEmail());
                    emails.add(user.getEmail());
                    currentUser.setEmail(user.getEmail());
                }
            }
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
