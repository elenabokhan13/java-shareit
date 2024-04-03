package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ServerErrorException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImp(userRepository, userMapper);
    }

    @Test
    void createUserTest() {
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        UserDto userOneDto = UserDto.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        when(userRepository.save(any())).thenReturn(userOne);
        when(userMapper.userToDto(any())).thenReturn(userOneDto);
        UserDto userReturn = userService.createUser(userMapper.userToDto(userOne));
        assertEquals(userOne.getName(), userReturn.getName());
        verify(userRepository).save(any());
    }

    @Test
    void updateUserTest() {
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        User userTwo = User.builder()
                .name("User2")
                .email("user1@user.om")
                .build();
        UserDto userOneDto = UserDto.builder()
                .name("User2")
                .email("user1@user.om")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(userRepository.save(any(User.class))).thenReturn(userTwo);
        when(userMapper.userToDto(any(User.class))).thenReturn(userOneDto);

        UserDto returnUser = userService.updateUser(userOneDto, 1L);
        assertEquals(userOneDto.getName(), returnUser.getName());
    }

    @Test
    void updateUserThrowsObjectNotFoundExceptionTest() {
        UserDto userOneDto = UserDto.builder()
                .name("User2")
                .email("user1@user.om")
                .build();

        when(userRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException("Данный пользователь не существет"));

        assertThrows(ObjectNotFoundException.class, () -> userService.updateUser(userOneDto, 1L));
    }

    @Test
    void updateUserThrowsServerErrorExceptionTest() {
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        User userTwo = User.builder()
                .name("User2")
                .email("user1@user.om")
                .build();
        UserDto userOneDto = UserDto.builder()
                .name("User2")
                .email("user2@user.om")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(userRepository.findByEmail(anyString())).thenReturn(userTwo);

        assertThrows(ServerErrorException.class, () -> userService.updateUser(userOneDto, 1L));
    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(anyLong());
    }

    @Test
    void getUsersTest() {
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        UserDto userOneDto = UserDto.builder()
                .name("User1")
                .email("user1@user.om")
                .build();

        when(userRepository.findAll()).thenReturn(List.of(userOne));
        when(userMapper.userToDto(any(User.class))).thenReturn(userOneDto);

        Collection<UserDto> response = userService.getUsers();
        assertEquals(response.size(), 1);
        assert (response.contains(userOneDto));
    }

    @Test
    void getUserByIdTest() {
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        UserDto userOneDto = UserDto.builder()
                .name("User1")
                .email("user1@user.om")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(userMapper.userToDto(any(User.class))).thenReturn(userOneDto);

        UserDto responseUser = userService.getUserById(1L);
        assertEquals(responseUser, userOneDto);
    }

    @Test
    void getUserByIdThrowsObjectNotFoundExceptionTest() {
        when(userRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException("Данный пользователь не существет"));

        assertThrows(ObjectNotFoundException.class, () -> userService.getUserById(1L));
    }
}