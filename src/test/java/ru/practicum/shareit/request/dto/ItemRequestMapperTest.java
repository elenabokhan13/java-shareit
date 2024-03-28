package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {
    private ItemRequestMapper itemRequestMapper;

    @BeforeEach
    public void setUp() {
        itemRequestMapper = new ItemRequestMapper();
    }

    @Test
    void itemRequestToDtoTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .userId(1L)
                .description("Book")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .userId(1L)
                .description("Book")
                .build();
        ItemRequestDto response = itemRequestMapper.itemRequestToDto(itemRequest);
        assertEquals(response, itemRequestDto);
    }

    @Test
    void itemRequestFromDto() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .userId(1L)
                .description("Book")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .userId(1L)
                .description("Book")
                .build();
        ItemRequest response = itemRequestMapper.itemRequestFromDto(itemRequestDto);
        assertEquals(response, itemRequest);
    }
}