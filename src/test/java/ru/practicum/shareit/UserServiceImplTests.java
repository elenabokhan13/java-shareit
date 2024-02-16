package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImp;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceImplTests {
    private UserServiceImp userServiceImp;
    private UserDto userOne;
    private UserDto userTwo;

    @BeforeEach
    public void createMeta() {
        UserStorage userStorage = new UserStorageImpl();
        UserMapper userMapper = new UserMapper();
        userServiceImp = new UserServiceImp(userStorage, userMapper);
        userOne = UserDto.builder()
                .name("User1")
                .email("user1@user.om")
                .build();

        userTwo = UserDto.builder()
                .name("User2")
                .email("user2@user.om")
                .build();
    }

    @Test
    public void testCreateUser() {
        UserDto userCreated = userServiceImp.createUser(userOne);
        userOne.setId(1L);
        assertThat(userCreated).isEqualTo(userOne);
    }

    @Test
    public void testUpdateUser() {
        userServiceImp.createUser(userOne);
        UserDto userCreated = userServiceImp.updateUser(userTwo, 1L);
        userTwo.setId(1L);
        assertThat(userCreated).isEqualTo(userTwo);
    }

    @Test
    public void testDeleteUser() {
        userServiceImp.createUser(userOne);
        userServiceImp.deleteUser(1L);
        assertThrows(NullPointerException.class, () -> userServiceImp.getUserById(1L));
    }

    @Test
    public void testGetUserById() {
        userServiceImp.createUser(userOne);
        userOne.setId(1L);
        assertThat(userServiceImp.getUserById(1L)).isEqualTo(userOne);
    }

    @Test
    public void testGetUsersById() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        userOne.setId(1L);
        userTwo.setId(2L);
        assertThat(userServiceImp.getUsers()).isEqualTo(List.of(userOne, userTwo));
    }
}
