package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoOutcoming {
    Item item;
    User booker;
    LocalDateTime start;
    LocalDateTime end;
    String status;
    Long id;
}
