package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {

    @NotNull(message = "Id предмета не может быть пустым.")
    private long itemId;

    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом.")
    @NotNull(message = "Время начала бронирования не может быть пустым.")
    private LocalDateTime start;

    @Future(message = "Время окончания бронирования не может быть в прошлом.")
    @NotNull(message = "Время окончания бронирования не может быть пустым.")
    private LocalDateTime end;
}