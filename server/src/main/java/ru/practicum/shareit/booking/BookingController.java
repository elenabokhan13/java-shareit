package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingDtoOutcoming;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.constant.Constant.USER_ID;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDtoOutcoming createBooking(@RequestBody BookingDtoIncoming bookingDto,
                                             @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /bookings для создания бронирования");
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping(value = "/{bookingId}", params = "approved")
    public BookingDtoOutcoming changeStatusBooking(@RequestHeader(USER_ID) Long userId,
                                                   @PathVariable Long bookingId, @RequestParam String approved) {
        log.info("Получен запрос к эндпойнту /bookings для обновления бронирования {}", bookingId);
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoOutcoming getBookingById(@RequestHeader(USER_ID) Long userId, @PathVariable Long bookingId) {
        log.info("Получен запрос к эндпойнту /bookings для получение информации о бронировании {}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public Collection<BookingDtoOutcoming> getUserBookings(@RequestHeader(USER_ID) Long userId,
                                                           @RequestParam(defaultValue = "ALL") String state,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпойнту /bookings для получение информации о бронированиях пользователя {}",
                userId);
        return bookingService.getAllByUser(userId, state, from, size);
    }

    @GetMapping(value = "/owner")
    public Collection<BookingDtoOutcoming> getOwnerBookings(@RequestHeader(USER_ID) Long userId,
                                                            @RequestParam(defaultValue = "ALL") String state,
                                                            @RequestParam(defaultValue = "0") int from,
                                                            @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпойнту /bookings для получение информации о бронированиях вещей пользователя {}",
                userId);
        return bookingService.getAllByOwner(userId, state, from, size);
    }
}
