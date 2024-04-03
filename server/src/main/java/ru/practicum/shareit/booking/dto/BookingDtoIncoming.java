package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoIncoming {

    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
