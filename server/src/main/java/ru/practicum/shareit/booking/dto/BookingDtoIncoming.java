package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoIncoming {

    @NotNull(message = "Id предмета не может быть пустым.")
    Long itemId;

    @NotNull(message = "Время начала бронирования не может быть пустым.")
    @Future(message = "Время начала бронирования не может быть в прошлом.")
    LocalDateTime start;

    @NotNull(message = "Время окончания бронирования не может быть пустым.")
    @Future(message = "Время окончания бронирования не может быть в прошлом.")
    LocalDateTime end;
}
