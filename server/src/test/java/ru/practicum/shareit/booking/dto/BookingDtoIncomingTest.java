package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoIncomingTest {
    @Autowired
    JacksonTester<BookingDtoIncoming> tester;

    @Test
    public void test() throws IOException {
        LocalDateTime start = LocalDateTime.of(2024, Month.APRIL, 10, 12, 01, 01);
        LocalDateTime end = LocalDateTime.of(2024, Month.APRIL, 12, 12, 01, 01);
        BookingDtoIncoming bookingDtoIncoming = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();
        JsonContent<BookingDtoIncoming> result = tester.write(bookingDtoIncoming);

        System.out.println(result);

        assertThat(result).hasJsonPath("$.itemId");
        assertThat(result).extractingJsonPathValue("$.itemId").isEqualTo(1);

        assertThat(result).hasJsonPath("$.start");
        assertThat(result).extractingJsonPathValue("$.start")
                .isEqualTo(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        assertThat(result).hasJsonPath("$.end");
        assertThat(result).extractingJsonPathValue("$.end")
                .isEqualTo(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}