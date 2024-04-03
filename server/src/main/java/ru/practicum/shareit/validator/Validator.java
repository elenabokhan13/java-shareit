package ru.practicum.shareit.validator;

import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Objects;

public class Validator {
    public static void validateUser(UserRepository userRepository, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
    }

    public static User validateUserAndReturn(UserRepository userRepository, Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Данный пользователь не существет"));
    }

    public static Item validateItemAndReturn(ItemRepository itemRepository, Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Данный предмет не существет"));
    }

    public static Booking validateBookingAndReturn(BookingRepository bookingRepository, Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Данное бронирование не существет"));
    }

    public static void validateRequest(ItemRequestRepository itemRequestRepository, Long requestId) {
        if (!itemRequestRepository.existsById(requestId)) {
            throw new ObjectNotFoundException("Данный реквест не существет");
        }
    }

    public static void validateItem(ItemRepository itemRepository, Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException("Данный предмет не существет");
        }
    }

    public static void bookingValidation(BookingDtoIncoming bookingDto, Item item, Long userId) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new InvalidRequestException("Время окончания бронирования не может быть раньше " +
                    "времени начала бронировани");
        }
        if (bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new InvalidRequestException("Время окончания бронирования не может быть равно времени " +
                    "начала бронировани");
        }
        if (!item.getAvailable()) {
            throw new InvalidRequestException("Данный предмет не доступен для бронирования");
        }
        if (Objects.equals(item.getOwnerId(), userId)) {
            throw new ObjectNotFoundException("Пользователь не может забронировать свой предмет");
        }
    }
}
