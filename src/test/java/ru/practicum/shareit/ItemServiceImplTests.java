package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbiddenError;
import ru.practicum.shareit.exception.InvalidRequestException;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Sql("/schema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTests {
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
    ItemDto itemOne;
    ItemDto itemTwo;


    @BeforeEach
    public void createMeta() {
        commentMapper = new CommentMapper();
        itemMapper = new ItemMapper();
        userMapper = new UserMapper();
        itemService = new ItemServiceImpl(itemRepository, userRepository,
                bookingRepository, itemMapper,
                commentRepository, commentMapper);
        userServiceImp = new UserServiceImp(userRepository, userMapper);

        userOne = UserDto.builder()
                .name("User1")
                .email("user1@user.om")
                .build();

        userTwo = UserDto.builder()
                .name("User2")
                .email("user2@user.om")
                .build();

        itemOne = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        itemTwo = ItemDto.builder()
                .name("item2")
                .description("description item 2")
                .available(true).build();
    }

    @Test
    public void testCreateItem() {
        userServiceImp.createUser(userOne);
        ItemDto itemCreated = itemService.createItem(itemOne, 1L);
        itemOne.setId(1L);
        assertThat(itemCreated).isEqualTo(itemOne);
    }

    @Test
    public void testUpdateItem() {
        userServiceImp.createUser(userOne);
        itemService.createItem(itemOne, 1L);
        ItemDto itemCreated = itemService.updateItem(1L, itemTwo, 1L);
        itemTwo.setId(1L);
        assertThat(itemCreated).isEqualTo(itemTwo);
    }

    @Test
    public void testGetItemById() {
        userServiceImp.createUser(userOne);
        itemService.createItem(itemOne, 1L);
        itemOne.setId(1L);
        itemOne.setComments(List.of());
        assertThat(itemService.getItem(1L, 1L)).isEqualTo(itemOne);
    }

    @Test
    public void testGetItemsByUserId() {
        userServiceImp.createUser(userOne);
        itemService.createItem(itemOne, 1L);
        itemService.createItem(itemTwo, 1L);
        itemOne.setId(1L);
        itemOne.setComments(List.of());
        itemTwo.setId(2L);
        itemTwo.setComments(List.of());
        assertThat(itemService.getItemsByUser(1L)).isEqualTo(List.of(itemOne, itemTwo));
    }

    @Test
    public void testSearchItemsByRequest() {
        userServiceImp.createUser(userOne);
        itemService.createItem(itemOne, 1L);
        itemService.createItem(itemTwo, 1L);
        itemOne.setId(1L);
        itemOne.setComments(List.of());
        assertThat(itemService.searchItems("item1")).isEqualTo(List.of(itemOne));
    }

    @Test
    public void testUpdateOtherUserItem() {
        userServiceImp.createUser(userOne);
        userServiceImp.createUser(userTwo);
        itemService.createItem(itemOne, 1L);
        assertThrows(AccessForbiddenError.class, () -> itemService.updateItem(1L, itemTwo, 2L));
    }

    @Test
    public void testUpdateItemNoUserId() {
        userServiceImp.createUser(userOne);
        itemService.createItem(itemOne, 1L);
        assertThrows(InvalidRequestException.class, () -> itemService.updateItem(1L, itemTwo, null));
    }

    @Test
    public void createItemNoUser() {
        assertThrows(ObjectNotFoundException.class, () -> itemService.createItem(itemOne, 1L));
    }

    @Test
    public void createItemNullUser() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> itemService.createItem(itemOne, null));
    }
}
