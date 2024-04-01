package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDto {
    Long id;
    String name;

    @Email(message = "Введите верный имейл")
    @NotBlank(message = "Введите верный имейл")
    @NotEmpty(message = "Введите верный имейл")
    String email;
}
