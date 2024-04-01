package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ItemRequestDto {

    Long id;

    Long userId;

    @NotBlank(message = "Введите описание предмета")
    @NotEmpty(message = "Введите описание предмета")
    String description;

    @Builder.Default
    List<ItemDto> items = new ArrayList<>();

    LocalDateTime created;
}
