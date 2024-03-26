package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {
    public BookingDtoOutcoming bookingDto(Booking booking) {
        return BookingDtoOutcoming.builder()
                .item(booking.getItem())
                .booker(booking.getBooker())
                .end(booking.getEndDate())
                .start(booking.getStartDate())
                .status(booking.getStatus())
                .id(booking.getId())
                .build();
    }
}
