package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoOutcoming {
    ItemDto item;
    UserDto booker;
    LocalDateTime start;
    LocalDateTime end;
    String status;
    Long id;
}
