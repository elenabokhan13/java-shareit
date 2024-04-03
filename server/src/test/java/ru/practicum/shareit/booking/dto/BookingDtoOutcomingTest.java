package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoOutcomingTest {
    @Autowired
    JacksonTester<BookingDtoOutcoming> tester;

    @Test
    public void test() throws IOException {
        LocalDateTime start = LocalDateTime.of(2024, Month.APRIL, 10, 12, 01, 01);
        LocalDateTime end = LocalDateTime.of(2024, Month.APRIL, 12, 12, 01, 01);

        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("Велосипед")
                .description("Почти новый велосипед")
                .available(true)
                .build();

        UserDto user = UserDto.builder()
                .id(1L)
                .name("Паша")
                .email("pasha@yandex.ru")
                .build();

        BookingDtoOutcoming bookingDtoOutcoming = BookingDtoOutcoming.builder()
                .item(item)
                .booker(user)
                .start(start)
                .end(end)
                .status("APPROVED")
                .id(1L)
                .build();
        JsonContent<BookingDtoOutcoming> result = tester.write(bookingDtoOutcoming);

        assertThat(result).hasJsonPath("$.item.id");
        assertThat(result).extractingJsonPathValue("$.item.id").isEqualTo(1);

        assertThat(result).hasJsonPath("$.start");
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(start
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        assertThat(result).hasJsonPath("$.end");
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(end
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        assertThat(result).hasJsonPath("$.status");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
    }
}