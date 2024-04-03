package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ItemRequestDto {

    Long id;

    Long userId;

    String description;

    @Builder.Default
    List<ItemDto> items = new ArrayList<>();

    LocalDateTime created;
}
