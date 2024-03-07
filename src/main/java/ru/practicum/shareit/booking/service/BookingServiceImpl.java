package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
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
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Данный пользователь не существет"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Данный предмет не существет"));
        bookingValidation(bookingDto, item, userId);
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
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Данное бронирование не существет"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Данный предмет не существет"));
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
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Данное бронирование не существет"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Данный предмет не существет"));
        if (!Objects.equals(item.getOwnerId(), userId) && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new ObjectNotFoundException("У данного пользователя нет доступа к бронированию");
        }
        return bookingMapper.bookingDto(booking);
    }

    @Override
    public Collection<BookingDtoOutcoming> getAllByUser(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        try {
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findByBookerIdOrderByStartDateDesc(userId).stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userId,
                            currentTime, currentTime).stream().map(bookingMapper::bookingDto).collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findByBookerIdAndEndDateBeforeOrderByStartDateDesc(userId, currentTime)
                            .stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findByBookerIdAndStartDateAfterOrderByStartDateDesc(userId, currentTime)
                            .stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case WAITING:
                    return bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDateDesc(userId, "WAITING")
                            .stream().map(bookingMapper::bookingDto).collect(Collectors.toList());
                case REJECTED:
                    return bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDateDesc(userId, "REJECTED")
                            .stream().map(bookingMapper::bookingDto).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new InvalidRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return List.of();
    }

    @Override
    public Collection<BookingDtoOutcoming> getAllByOwner(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        try {
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepository.findByOwnerId(userId).stream().map(bookingMapper::bookingDto)
                            .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findByOwnerIdCurrent(userId, currentTime, currentTime).stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findByOwnerIdPast(userId, currentTime).stream()
                            .map(bookingMapper::bookingDto)
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findByOwnerIdFuture(userId, currentTime).stream()
                            .map(bookingMapper::bookingDto)
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepository.findByOwnerIdStatus(userId, "WAITING").stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
                case REJECTED:
                    return bookingRepository.findByOwnerIdStatus(userId, "REJECTED").stream()
                            .map(bookingMapper::bookingDto).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new InvalidRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return List.of();
    }

    private void bookingValidation(BookingDtoIncoming bookingDto, Item item, Long userId) {
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
