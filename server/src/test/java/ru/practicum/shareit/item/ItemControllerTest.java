package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constant.Constant.USER_ID;


@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Test
    void createItemTest() throws Exception {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemService.createItem(any(), anyLong())).thenReturn(itemOneDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemOneDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemOneDto.getName()))
                .andExpect(jsonPath("$.description").value(itemOneDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemOneDto.getAvailable()));
    }

    @Test
    void updateItemTest() throws Exception {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemService.updateItem(anyLong(), any(), anyLong())).thenReturn(itemOneDto);

        mvc.perform(patch("/items/{itemId}", "1")
                        .content(mapper.writeValueAsString(itemOneDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemOneDto.getName()))
                .andExpect(jsonPath("$.description").value(itemOneDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemOneDto.getAvailable()));
    }

    @Test
    void getItemTest() throws Exception {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemService.getItem(anyLong(), anyLong())).thenReturn(itemOneDto);

        mvc.perform(get("/items/{itemId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemOneDto.getName()))
                .andExpect(jsonPath("$.description").value(itemOneDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemOneDto.getAvailable()));
    }

    @Test
    void getItemsByUserTest() throws Exception {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemService.getItemsByUser(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemOneDto));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]name").value(itemOneDto.getName()))
                .andExpect(jsonPath("$.[0]description").value(itemOneDto.getDescription()))
                .andExpect(jsonPath("$.[0]available").value(itemOneDto.getAvailable()));
    }

    @Test
    void searchItems() throws Exception {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemService.searchItems(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemOneDto));

        mvc.perform(get("/items/search")
                        //   .content(mapper.writeValueAsString(itemOneDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L)
                        .param("text", "description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]name").value(itemOneDto.getName()))
                .andExpect(jsonPath("$.[0]description").value(itemOneDto.getDescription()))
                .andExpect(jsonPath("$.[0]available").value(itemOneDto.getAvailable()));
    }

    @Test
    void postCommentTest() throws Exception {
        CommentDto commentOne = CommentDto.builder()
                .text("Comment 1")
                .build();

        when(commentService.addComment(anyLong(), anyLong(), any())).thenReturn(commentOne);

        mvc.perform(post("/items/{itemId}/comment", "1")
                        .content(mapper.writeValueAsString(commentOne))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentOne.getText()));
    }
}