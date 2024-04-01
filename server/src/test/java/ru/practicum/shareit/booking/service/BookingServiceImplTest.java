package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingDtoOutcoming;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService bookingService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingMapper bookingMapper;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceImpl(userRepository, bookingRepository, itemRepository, bookingMapper);
    }

    @Test
    void createBookingTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingOne);
        when(bookingMapper.bookingDto(bookingOne)).thenReturn(bookingOneDtoOut);

        BookingDtoOutcoming response = bookingService.createBooking(bookingOneDto, 1L);
        assertEquals(response, bookingOneDtoOut);
    }

    @Test
    void createBookingThrowsObjectNotFoundExceptionForUserTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();

        when(userRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException("Данный пользователь не существет"));

        assertThrows(ObjectNotFoundException.class, () -> bookingService.createBooking(bookingOneDto, 1L));
    }

    @Test
    void createBookingThrowsObjectNotFoundExceptionForItemTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(itemRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Данный предмет не существет"));

        assertThrows(ObjectNotFoundException.class, () -> bookingService.createBooking(bookingOneDto, 1L));
    }

    @Test
    void updateBookingTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        Booking bookingTwo = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingTwoDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(bookingRepository.save(any())).thenReturn(bookingTwo);
        when(bookingMapper.bookingDto(any())).thenReturn(bookingTwoDtoOut);

        BookingDtoOutcoming response = bookingService.updateBooking(1L, 1L, "true");
        assertEquals(response, bookingTwoDtoOut);
    }

    @Test
    void updateBookingRejectedTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        Booking bookingTwo = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingTwoDtoOut = BookingDtoOutcoming.builder()
                .status("REJECTED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(bookingRepository.save(any())).thenReturn(bookingTwo);
        when(bookingMapper.bookingDto(any())).thenReturn(bookingTwoDtoOut);

        BookingDtoOutcoming response = bookingService.updateBooking(1L, 1L, "false");
        assertEquals(response, bookingTwoDtoOut);
    }

    @Test
    void updateBookingThrowObjectNotFoundExceptionForUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> bookingService
                .updateBooking(1L, 1L, "true"));
    }

    @Test
    void updateBookingThrowObjectNotFoundExceptionForBookingTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException("Данное бронирование не существет"));

        assertThrows(ObjectNotFoundException.class, () -> bookingService
                .updateBooking(1L, 1L, "true"));
    }

    @Test
    void updateBookingThrowObjectNotFoundExceptionForItemTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        when(itemRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Данный предмет не существет"));

        assertThrows(ObjectNotFoundException.class, () -> bookingService
                .updateBooking(1L, 1L, "true"));
    }

    @Test
    void updateBookingThrowObjectNotFoundExceptionForAccessTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(2L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));

        assertThrows(ObjectNotFoundException.class, () -> bookingService
                .updateBooking(1L, 1L, "true"));
    }

    @Test
    void updateBookingThrowsInvalidRequestExceptionTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingTwo = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingTwo));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));

        assertThrows(InvalidRequestException.class, () -> bookingService
                .updateBooking(1L, 1L, "true"));
    }

    @Test
    void getBookingByIdTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(bookingMapper.bookingDto(any(Booking.class))).thenReturn(bookingOneDtoOut);

        BookingDtoOutcoming response = bookingService.getBookingById(1L, 1L);
        assertEquals(response, bookingOneDtoOut);
    }

    @Test
    void getBookingByIdThrowsObjectNotFoundExceptionForUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBookingByIdThrowsObjectNotFoundExceptionForBookingTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException("Данное бронирование не существет"));

        assertThrows(ObjectNotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBookingByIdThrowsObjectNotFoundExceptionForItemTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        when(itemRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Данный предмет не существет"));

        assertThrows(ObjectNotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBookingByIdThrowsObjectNotFoundExceptionForAccessTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));

        assertThrows(ObjectNotFoundException.class, () -> bookingService.getBookingById(3L, 3L));
    }

    @Test
    void getAllByUserTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByBookerIdOrderByStartDate(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);

        Collection<BookingDtoOutcoming> response = bookingService.getAllByUser(1L, "ALL", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByUserCurrentTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);
        when(bookingRepository.findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(any(), any(), any(),
                any())).thenReturn(new PageImpl<>(List.of(bookingOne)));

        Collection<BookingDtoOutcoming> response = bookingService.getAllByUser(1L, "CURRENT", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByUserPastTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.MARCH, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);
        when(bookingRepository.findByBookerIdAndEndDateBeforeOrderByStartDateDesc(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));

        Collection<BookingDtoOutcoming> response = bookingService.getAllByUser(1L, "PAST", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByUserFutureTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.MAY, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);
        when(bookingRepository.findByBookerIdAndStartDateAfterOrderByStartDateDesc(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));

        Collection<BookingDtoOutcoming> response = bookingService.getAllByUser(1L, "FUTURE", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByUserWaitingTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.MAY, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);
        when(bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDateDesc(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));

        Collection<BookingDtoOutcoming> response = bookingService.getAllByUser(1L, "WAITING", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByUserRejectedTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.MAY, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("REJECTED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("REJECTED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);
        when(bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDateDesc(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));

        Collection<BookingDtoOutcoming> response = bookingService.getAllByUser(1L, "REJECTED", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByUserThrowsObjectNotFoundExceptionTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllByUser(1L, "ALL", 0, 10));
    }

    @Test
    void getAllByUserThrowsInvalidRequestExceptionForSizeTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> bookingService
                .getAllByUser(1L, "ALL", 0, 0));
    }

    @Test
    void getAllByUserThrowsInvalidRequestExceptionForFromTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> bookingService
                .getAllByUser(1L, "ALL", -1, 10));
    }

    @Test
    void getAllByUserThrowsInvalidRequestExceptionForStateTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> bookingService
                .getAllByUser(1L, "AKK", 0, 10));
    }

    @Test
    void getAllByOwnerTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByOwnerId(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingOne)));
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);

        Collection<BookingDtoOutcoming> response = bookingService.getAllByOwner(1L, "ALL", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByOwnerCurrentTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByOwnerIdCurrent(anyLong(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);

        Collection<BookingDtoOutcoming> response = bookingService.getAllByOwner(1L, "CURRENT", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByOwnerPastTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.FEBRUARY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.FEBRUARY, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByOwnerIdPast(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);

        Collection<BookingDtoOutcoming> response = bookingService.getAllByOwner(1L, "PAST", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByOwnerFutureTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.MAY, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .id(1L)
                .name("item1")
                .ownerId(1L)
                .description("description item 1")
                .available(true).build();
        Booking bookingOne = Booking.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .endDate(bookingOneDto.getEnd())
                .startDate(bookingOneDto.getStart())
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByOwnerIdFuture(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookingOne)));
        when(bookingMapper.bookingDto(any())).thenReturn(bookingOneDtoOut);

        Collection<BookingDtoOutcoming> response = bookingService.getAllByOwner(1L, "FUTURE", 0, 10);
        assertEquals(response.size(), 1);
        assertTrue(response.contains(bookingOneDtoOut));
    }

    @Test
    void getAllByOwnerThrowsObjectNotFoundExceptionTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> bookingService
                .getAllByOwner(1L, "ALL", 0, 10));
    }

    @Test
    void getAllByOwnerThrowsInvalidRequestExceptionForSizeTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> bookingService
                .getAllByOwner(1L, "ALL", 0, 0));
    }

    @Test
    void getAllByOwnerThrowsInvalidRequestExceptionForFromTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> bookingService
                .getAllByOwner(1L, "ALL", -1, 10));
    }

    @Test
    void getAllByOwnerThrowsInvalidRequestExceptionForStateTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> bookingService
                .getAllByOwner(1L, "AKK", 0, 10));
    }

    @Test
    void bookingValidationThrowInvalidRequestExceptionForEndBeforeTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));

        assertThrows(InvalidRequestException.class, () -> bookingService.createBooking(bookingOneDto, 1L));
    }

    @Test
    void bookingValidationThrowInvalidRequestExceptionForEndEqualsStartTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));

        assertThrows(InvalidRequestException.class, () -> bookingService.createBooking(bookingOneDto, 1L));
    }

    @Test
    void bookingValidationThrowInvalidRequestExceptionForNotAvailableTest() {
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MAY, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.JULY, 5, 23, 23))
                .build();
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .available(false).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));

        assertThrows(InvalidRequestException.class, () -> bookingService.createBooking(bookingOneDto, 1L));
    }
}