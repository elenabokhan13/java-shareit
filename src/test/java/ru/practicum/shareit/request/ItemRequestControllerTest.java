package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.ItemController.USER_ID;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void createRequestTest() throws Exception {
        ItemRequestDto requestOutcoming = ItemRequestDto.builder()
                .description("Нужен самокат")
                .userId(1L)
                .id(1L)
                .created(LocalDateTime.now())
                .build();
        ItemRequestDto requestIncoming = ItemRequestDto.builder()
                .description("Нужен самокат")
                .build();

        when(itemRequestService.createItemRequest(any(), anyLong())).thenReturn(requestOutcoming);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestIncoming))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(requestOutcoming.getDescription()))
                .andExpect(jsonPath("$.userId").value(requestOutcoming.getUserId()))
                .andExpect(jsonPath("$.id").value(requestOutcoming.getId()));
    }

    @Test
    void getAllUserRequestsTest() throws Exception {
        ItemRequestDto requestOutcoming = ItemRequestDto.builder()
                .description("Нужен самокат")
                .userId(1L)
                .id(1L)
                .created(LocalDateTime.now())
                .build();

        when(itemRequestService.getAllUserRequests(anyLong())).thenReturn(List.of(requestOutcoming));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]description").value(requestOutcoming.getDescription()))
                .andExpect(jsonPath("$.[0]userId").value(requestOutcoming.getUserId()))
                .andExpect(jsonPath("$.[0]id").value(requestOutcoming.getId()));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        ItemRequestDto requestOutcoming = ItemRequestDto.builder()
                .description("Нужен самокат")
                .userId(1L)
                .id(1L)
                .created(LocalDateTime.now())
                .build();

        when(itemRequestService.getAllRequests(anyInt(), anyInt(), anyLong())).thenReturn(List.of(requestOutcoming));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]description").value(requestOutcoming.getDescription()))
                .andExpect(jsonPath("$.[0]userId").value(requestOutcoming.getUserId()))
                .andExpect(jsonPath("$.[0]id").value(requestOutcoming.getId()));
    }

    @Test
    void getItemRequestTest() throws Exception {
        ItemRequestDto requestOutcoming = ItemRequestDto.builder()
                .description("Нужен самокат")
                .userId(1L)
                .id(1L)
                .created(LocalDateTime.now())
                .build();

        when(itemRequestService.getItemRequest(anyLong(), anyLong())).thenReturn(requestOutcoming);

        mvc.perform(get("/requests/{requestId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(requestOutcoming.getDescription()))
                .andExpect(jsonPath("$.userId").value(requestOutcoming.getUserId()))
                .andExpect(jsonPath("$.id").value(requestOutcoming.getId()));
    }
}