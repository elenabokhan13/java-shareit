package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {
    @Autowired
    JacksonTester<ItemDto> tester;

    @Test
    public void test() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Велосипед")
                .description("Почти новый велосипед")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .comments(List.of())
                .build();
        JsonContent<ItemDto> result = tester.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);

        assertThat(result).hasJsonPath("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Велосипед");

        assertThat(result).hasJsonPath("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Почти новый велосипед");

        assertThat(result).hasJsonPath("$.available");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).extractingJsonPathValue("$.lastBooking").isEqualTo(null);

        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).extractingJsonPathValue("$.nextBooking").isEqualTo(null);

        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).extractingJsonPathArrayValue("$.comments").isEqualTo(List.of());
    }

}