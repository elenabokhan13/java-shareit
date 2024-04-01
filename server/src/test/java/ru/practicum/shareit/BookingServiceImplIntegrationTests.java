package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingDtoOutcoming;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImp;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql("/schema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplIntegrationTests {
    private BookingServiceImpl bookingService;
    private ItemServiceImpl itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private ItemMapper itemMapper;
    private UserMapper userMapper;
    @Autowired
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    private UserServiceImp userServiceImp;
    private UserDto userOne;
    private UserDto userTwo;

    private UserDto userThree;
    private ItemDto itemOne;
    ItemDto itemTwo;
    BookingDtoIncoming bookingOne;
    BookingDtoIncoming bookingTwo;

    @BeforeEach
    public void createMeta() {
        commentMapper = new CommentMapper();
        itemMapper = new ItemMapper();
        userMapper = new UserMapper();
        bookingMapper = new BookingMapper();
        itemService = new ItemServiceImpl(itemRepository, userRepository,
                bookingRepository, itemMapper,
                commentRepository, commentMapper);
        userServiceImp = new UserServiceImp(userRepository, userMapper);

        bookingService = new BookingServiceImpl(userRepository, bookingRepository,
                itemRepository, bookingMapper);

        userOne = UserDto.builder()
                .name("User1")
                .email("user1@user.om")
                .build();

        userTwo = UserDto.builder()
                .name("User2")
                .email("user2@user.om")
                .build();

        userThree = UserDto.builder()
                .name("User3")
                .email("user3@user.om")
                .build();

        itemOne = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        itemTwo = ItemDto.builder()
                .name("item2")
                .description("description item 2")
                .available(true).build();

        bookingOne = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.MARCH, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
    }

    @Test
    public void testCreateBooking() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);
        BookingDtoOutcoming bookingDtoOutcoming = bookingService.createBooking(bookingOne, 2L);

        assertThat(bookingDtoOutcoming.getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.getBooker().getId()).isEqualTo(2L);
        assertThat(bookingDtoOutcoming.getItem().getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.getStatus()).isEqualTo("WAITING");
    }

    @Test
    public void testUpdateBooking() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);
        bookingService.createBooking(bookingOne, 2L);
        BookingDtoOutcoming bookingDtoOutcoming = bookingService.updateBooking(1L, 1L, "true");

        assertThat(bookingDtoOutcoming.getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.getBooker().getId()).isEqualTo(2L);
        assertThat(bookingDtoOutcoming.getItem().getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.getStatus()).isEqualTo("APPROVED");
    }

    @Test
    public void testGetBookingById() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);
        bookingService.createBooking(bookingOne, 2L);
        BookingDtoOutcoming bookingDtoOutcoming = bookingService.getBookingById(1L, 1L);

        assertThat(bookingDtoOutcoming.getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.getBooker().getId()).isEqualTo(2L);
        assertThat(bookingDtoOutcoming.getItem().getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.getStatus()).isEqualTo("WAITING");
    }

    @Test
    public void testGetAllByUser() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);
        bookingService.createBooking(bookingOne, 2L);
        Collection<BookingDtoOutcoming> bookingDtoOutcoming = bookingService
                .getAllByUser(2L, "ALL", 0, 10);

        assertThat(bookingDtoOutcoming.size()).isEqualTo(1);
        assertThat(bookingDtoOutcoming.iterator().next().getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.iterator().next().getBooker().getId()).isEqualTo(2L);
        assertThat(bookingDtoOutcoming.iterator().next().getItem().getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.iterator().next().getStatus()).isEqualTo("WAITING");
    }

    @Test
    public void testGetAllByOwner() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);
        bookingService.createBooking(bookingOne, 2L);
        Collection<BookingDtoOutcoming> bookingDtoOutcoming = bookingService
                .getAllByOwner(1L, "ALL", 0, 10);

        assertThat(bookingDtoOutcoming.size()).isEqualTo(1);
        assertThat(bookingDtoOutcoming.iterator().next().getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.iterator().next().getBooker().getId()).isEqualTo(2L);
        assertThat(bookingDtoOutcoming.iterator().next().getItem().getId()).isEqualTo(1L);
        assertThat(bookingDtoOutcoming.iterator().next().getStatus()).isEqualTo("WAITING");
    }

    @Test
    public void testCreateBookingOwner() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);

        assertThrows(ObjectNotFoundException.class, () -> bookingService.createBooking(bookingOne, 1L));
    }

    @Test
    public void testGetBookingByIdNoAccesse() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        userServiceImp.createUser(userThree);
        itemService.createItem(itemOne, 1L);
        bookingService.createBooking(bookingOne, 2L);

        assertThrows(ObjectNotFoundException.class, () -> bookingService.getBookingById(1L, 3L));
    }
}
