package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImp;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Sql("/schema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImplTest {
    private CommentServiceImpl commentService;
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

    CommentDto commentOne;

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

        commentService = new CommentServiceImpl(commentMapper, bookingRepository,
                commentRepository, userRepository,
                itemRepository);

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
                .start(LocalDateTime.of(2024, Month.MARCH, 14, 23, 23, 1, 1))
                .end(LocalDateTime.of(2024, Month.MARCH, 15, 23, 23, 1, 2))
                .build();

        commentOne = CommentDto.builder()
                .text("Comment 1")
                .build();
    }

    @Test
    public void testCreateCommentEndBeforeStart() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);
        bookingService.createBooking(bookingOne, 2L);
        assertThrows(InvalidRequestException.class, () -> commentService.addComment(2L, 1L, commentOne));
    }
}
