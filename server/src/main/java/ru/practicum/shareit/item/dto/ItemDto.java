package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.List;

@Data
@Builder
public class ItemDto {
    Long id;

    String name;

    String description;

    BookingDtoShort lastBooking;

    BookingDtoShort nextBooking;

    Boolean available;

    Long requestId;

    List<CommentDto> comments;
}
