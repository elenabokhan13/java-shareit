package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;

    @Test
    void getUserByIdTest() throws Exception {
        UserDto userOutcoming = UserDto.builder()
                .name("Паша")
                .email("pasha@yandex.ru")
                .id(1L)
                .build();

        when(userService.getUserById(anyLong())).thenReturn(userOutcoming);

        mvc.perform(get("/users/{userId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userOutcoming.getName()))
                .andExpect(jsonPath("$.email").value(userOutcoming.getEmail()))
                .andExpect(jsonPath("$.id").value(userOutcoming.getId()));
    }

    @Test
    void getUsersTest() throws Exception {
        UserDto userOutcoming = UserDto.builder()
                .name("Паша")
                .email("pasha@yandex.ru")
                .id(1L)
                .build();

        when(userService.getUsers()).thenReturn(List.of(userOutcoming));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]name").value(userOutcoming.getName()))
                .andExpect(jsonPath("$.[0]email").value(userOutcoming.getEmail()))
                .andExpect(jsonPath("$.[0]id").value(userOutcoming.getId()));
    }

    @Test
    void createUserTest() throws Exception {
        UserDto userIncoming = UserDto.builder()
                .name("Паша")
                .email("pasha@yandex.ru")
                .build();
        UserDto userOutcoming = UserDto.builder()
                .name("Паша")
                .email("pasha@yandex.ru")
                .id(1L)
                .build();

        when(userService.createUser(any())).thenReturn(userOutcoming);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userIncoming))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userOutcoming.getName()))
                .andExpect(jsonPath("$.email").value(userOutcoming.getEmail()))
                .andExpect(jsonPath("$.id").value(userOutcoming.getId()));
    }

    @Test
    void updateUserTest() throws Exception {
        UserDto userIncoming = UserDto.builder()
                .name("Паша")
                .email("pasha@yandex.ru")
                .build();
        UserDto userOutcoming = UserDto.builder()
                .name("Паша")
                .email("pasha@yandex.ru")
                .id(1L)
                .build();

        when(userService.updateUser(any(), anyLong())).thenReturn(userOutcoming);

        mvc.perform(patch("/users/{userId}", "1")
                        .content(mapper.writeValueAsString(userIncoming))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userOutcoming.getName()))
                .andExpect(jsonPath("$.email").value(userOutcoming.getEmail()))
                .andExpect(jsonPath("$.id").value(userOutcoming.getId()));
    }

    @Test
    void deleteUserTest() throws Exception {
        mvc.perform(delete("/users/{userId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}