package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingDtoOutcoming;

import java.util.Collection;

public interface BookingService {
    BookingDtoOutcoming createBooking(BookingDtoIncoming bookingDto, Long userId);

    BookingDtoOutcoming updateBooking(Long userId, Long bookingId, String available);

    BookingDtoOutcoming getBookingById(Long bookingId, Long userId);

    Collection<BookingDtoOutcoming> getAllByUser(Long userId, String state);

    Collection<BookingDtoOutcoming> getAllByOwner(Long userId, String state);

}
