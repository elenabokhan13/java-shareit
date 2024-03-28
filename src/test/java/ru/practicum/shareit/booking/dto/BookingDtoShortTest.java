package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoShortTest {
    @Autowired
    JacksonTester<BookingDtoShort> tester;

    @Test
    public void test() throws IOException {
        BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
                .id(1L)
                .bookerId(1L)
                .build();
        JsonContent<BookingDtoShort> result = tester.write(bookingDtoShort);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);

        assertThat(result).hasJsonPath("$.bookerId");
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
    }

}