package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    JacksonTester<ItemRequestDto> tester;

    @Test
    public void test() throws IOException {
        LocalDateTime time = LocalDateTime.of(2024, Month.MARCH, 30, 12, 23, 9);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .userId(1L)
                .description("Ищу пылесос")
                .items(List.of())
                .created(time)
                .build();
        JsonContent<ItemRequestDto> result = tester.write(itemRequestDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);

        assertThat(result).hasJsonPath("$.userId");
        assertThat(result).extractingJsonPathValue("$.userId").isEqualTo(1);

        assertThat(result).hasJsonPath("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Ищу пылесос");

        assertThat(result).hasJsonPath("$.items");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(List.of());

        assertThat(result).hasJsonPath("$.created");
        assertThat(result).extractingJsonPathValue("$.created")
                .isEqualTo(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}