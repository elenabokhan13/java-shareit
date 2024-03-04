package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingDtoOutcoming;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;

import static ru.practicum.shareit.item.ItemController.USER_ID;

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
    public BookingDtoOutcoming createBooking(@Valid @RequestBody BookingDtoIncoming bookingDto,
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
                                                           @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос к эндпойнту /bookings для получение информации о бронированиях пользователя {}",
                userId);
        return bookingService.getAllByUser(userId, state);
    }

    @GetMapping(value = "/owner")
    public Collection<BookingDtoOutcoming> getOwnerBookings(@RequestHeader(USER_ID) Long userId,
                                                            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос к эндпойнту /bookings для получение информации о бронированиях вещей пользователя {}",
                userId);
        return bookingService.getAllByOwner(userId, state);
    }


}
