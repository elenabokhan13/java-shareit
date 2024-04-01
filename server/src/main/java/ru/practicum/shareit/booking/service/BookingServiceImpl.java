package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingDtoOutcoming;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.validator.Validator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(UserRepository userRepository, BookingRepository bookingRepository,
                              ItemRepository itemRepository, BookingMapper bookingMapper) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDtoOutcoming createBooking(BookingDtoIncoming bookingDto, Long userId) {
        User currentUser = Validator.validateUserAndReturn(userRepository, userId);
        Item item = Validator.validateItemAndReturn(itemRepository, bookingDto.getItemId());
        Validator.bookingValidation(bookingDto, item, userId);
        Booking booking = Booking.builder()
                .status("WAITING")
                .booker(currentUser)
                .item(item)
                .endDate(bookingDto.getEnd())
                .startDate(bookingDto.getStart())
                .build();
        booking.getBooker().setId(userId);
        return bookingMapper.bookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutcoming updateBooking(Long userId, Long bookingId, String available) {
        Validator.validateUser(userRepository, userId);
        Booking booking = Validator.validateBookingAndReturn(bookingRepository, bookingId);
        Item item = Validator.validateItemAndReturn(itemRepository, booking.getItem().getId());
        if (!Objects.equals(item.getOwnerId(), userId)) {
            throw new ObjectNotFoundException("У данного пользователя нет доступа к редактированию бронирования");
        }
        if (!Objects.equals(booking.getStatus(), "WAITING")) {
            throw new InvalidRequestException("Бронирование уже обработано");
        }
        if (Objects.equals(available, "true")) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        return bookingMapper.bookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutcoming getBookingById(Long bookingId, Long userId) {
        Validator.validateUser(userRepository, userId);
        Booking booking = Validator.validateBookingAndReturn(bookingRepository, bookingId);
        Item item = Validator.validateItemAndReturn(itemRepository, booking.getItem().getId());
        if (!Objects.equals(item.getOwnerId(), userId) && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new ObjectNotFoundException("У данного пользователя нет доступа к бронированию");
        }
        return bookingMapper.bookingDto(booking);
    }

    @Override
    public Collection<BookingDtoOutcoming> getAllByUser(Long userId, String state, int from, int size) {
        Validator.validateUser(userRepository, userId);
        Validator.validateSizeAndFrom(from, size);
        LocalDateTime currentTime = LocalDateTime.now();
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        try {
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findByBookerIdOrderByStartDateDesc(userId, pageable).stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userId,
                                    currentTime, currentTime, pageable).stream().map(bookingMapper::bookingDto)
                            .collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findByBookerIdAndEndDateBeforeOrderByStartDateDesc(userId, currentTime,
                            pageable).stream().map(bookingMapper::bookingDto).collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findByBookerIdAndStartDateAfterOrderByStartDateDesc(userId, currentTime,
                            pageable).stream().map(bookingMapper::bookingDto).collect(Collectors.toList());
                case WAITING:
                    return bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDateDesc(userId, "WAITING",
                            pageable).stream().map(bookingMapper::bookingDto).collect(Collectors.toList());
                case REJECTED:
                    return bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDateDesc(userId, "REJECTED",
                            pageable).stream().map(bookingMapper::bookingDto).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new InvalidRequestException("Unknown state");
        }

        return List.of();
    }

    @Override
    public Collection<BookingDtoOutcoming> getAllByOwner(Long userId, String state, int from, int size) {
        Validator.validateUser(userRepository, userId);
        Validator.validateSizeAndFrom(from, size);
        LocalDateTime currentTime = LocalDateTime.now();
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        try {
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findByOwnerId(userId, pageable).stream().map(bookingMapper::bookingDto)
                            .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findByOwnerIdCurrent(userId, currentTime, currentTime, pageable).stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findByOwnerIdPast(userId, currentTime, pageable).stream()
                            .map(bookingMapper::bookingDto)
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findByOwnerIdFuture(userId, currentTime, pageable).stream()
                            .map(bookingMapper::bookingDto)
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepository.findByOwnerIdStatus(userId, "WAITING", pageable).stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case REJECTED:
                    return bookingRepository.findByOwnerIdStatus(userId, "REJECTED", pageable).stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new InvalidRequestException("Unknown status");
        }

        return List.of();
    }
}
