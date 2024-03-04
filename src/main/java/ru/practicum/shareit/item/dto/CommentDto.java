package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    Long id;

    @NotBlank(message = "Введите комментарий")
    @NotEmpty(message = "Введите комментарий")
    String text;

    String authorName;

    LocalDateTime created;
}
