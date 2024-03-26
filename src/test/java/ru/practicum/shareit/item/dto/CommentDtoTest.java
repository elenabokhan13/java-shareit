package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {
    @Autowired
    JacksonTester<CommentDto> tester;

    @Test
    public void test() throws IOException {
        LocalDateTime time = LocalDateTime.now();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Отличный предмет")
                .authorName("Оля")
                .created(time)
                .build();
        JsonContent<CommentDto> result = tester.write(commentDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);

        assertThat(result).hasJsonPath("$.text");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Отличный предмет");

        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Оля");

        assertThat(result).hasJsonPath("$.created");
        assertThat(result).extractingJsonPathValue("$.created").isEqualTo(time.format(DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")));
    }
}