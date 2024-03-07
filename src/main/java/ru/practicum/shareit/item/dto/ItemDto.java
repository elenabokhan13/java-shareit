package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemDto {
    Long id;

    @NotBlank(message = "Введите название предмета")
    @NotEmpty(message = "Введите название предмета")
    String name;

    @NotBlank(message = "Введите описание предмета")
    @NotEmpty(message = "Введите описание предмета")
    String description;

    BookingDtoShort lastBooking;

    BookingDtoShort nextBooking;

    @NotNull(message = "Введите статус")
    Boolean available;

    List<CommentDto> comments;
}
