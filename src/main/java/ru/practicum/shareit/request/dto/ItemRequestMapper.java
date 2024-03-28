package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ItemRequestMapper {

    private final ItemMapper itemMapper = new ItemMapper();

    public ItemRequestDto itemRequestToDto(ItemRequest itemRequest) {
        ItemRequestDto response = ItemRequestDto.builder()
                .description(itemRequest.getDescription())
                .id(itemRequest.getId())
                .userId(itemRequest.getUserId())
                .created(itemRequest.getCreated())
                .build();
        Collection<Item> items = itemRequest.getItems();
        if (items != null) {
            response.setItems(items.stream().map(itemMapper::itemToDto).collect(Collectors.toList()));
        }
        return response;
    }

    public ItemRequest itemRequestFromDto(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .userId(itemRequestDto.getUserId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .build();
    }
}
