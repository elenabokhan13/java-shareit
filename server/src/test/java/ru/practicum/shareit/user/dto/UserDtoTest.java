package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> tester;

    @Test
    public void test() throws IOException {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Паша")
                .email("pasha@yandex.ru")
                .build();
        JsonContent<UserDto> result = tester.write(userDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathValue("$.id")
                .isEqualTo(1);

        assertThat(result).hasJsonPath("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Паша");

        assertThat(result).hasJsonPath("$.email");
        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo("pasha@yandex.ru");
    }

}