package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImp;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@Sql("/schema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTests {
    private UserServiceImp userServiceImp;
    @Autowired
    private UserRepository userRepository;

    private UserDto userOne;
    private UserDto userTwo;

    @BeforeEach
    public void createMeta() {
        UserMapper userMapper = new UserMapper();

        userServiceImp = new UserServiceImp(userRepository, userMapper);
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
        assertThrows(ObjectNotFoundException.class, () -> userServiceImp.getUserById(1L));
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
