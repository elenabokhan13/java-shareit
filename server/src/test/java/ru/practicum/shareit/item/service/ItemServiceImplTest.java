package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbiddenError;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, itemMapper,
                commentRepository, commentMapper);
    }

    @Test
    void createItemTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemMapper.dtoToItem(any(ItemDto.class))).thenReturn(itemOne);
        when(itemRepository.save(itemOne)).thenReturn(itemOne);
        when(itemMapper.itemToDto(itemOne)).thenReturn(itemOneDto);

        ItemDto response = itemService.createItem(itemOneDto, 1L);
        assertEquals(response, itemOneDto);
    }

    @Test
    void createItemThrowsObjectNotFoundExceptionTest() {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> itemService.createItem(itemOneDto, 1L));
    }

    @Test
    void updateItemTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .ownerId(1L)
                .available(true).build();
        Item itemTwo = Item.builder()
                .name("item2")
                .description("description item 2")
                .ownerId(1L)
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemTwo));
        assert itemTwo != null;
        when(itemRepository.save(itemTwo)).thenReturn(itemOne);
        when(itemMapper.itemToDto(itemOne)).thenReturn(itemOneDto);

        ItemDto response = itemService.updateItem(1L, itemOneDto, 1L);
        assertEquals(response, itemOneDto);
    }

    @Test
    void updateItemThrowsInvalidRequestExceptionTest() {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        assertThrows(InvalidRequestException.class, () -> itemService.updateItem(1L, itemOneDto, null));
    }

    @Test
    void updateItemThrowsObjectNotFoundExceptionTest() {
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Данный предмет не существет"));

        assertThrows(ObjectNotFoundException.class, () -> itemService.updateItem(1L, itemOneDto, 1L));
    }

    @Test
    void updateItemThrowsAccessForbiddenErrorTest() {
        Item itemTwo = Item.builder()
                .name("item2")
                .description("description item 2")
                .ownerId(1L)
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemTwo));

        assertThrows(AccessForbiddenError.class, () -> itemService.updateItem(1L, itemOneDto, 2L));
    }

    @Test
    void getItemTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .ownerId(1L)
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of());
        when(itemMapper.itemToDto(itemOne)).thenReturn(itemOneDto);

        ItemDto response = itemService.getItem(1L, 1L);
        assertEquals(response, itemOneDto);
    }

    @Test
    void getItemWithBookingsTest() {
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .ownerId(1L)
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();
        Booking bookingLast = Booking.builder()
                .booker(userOne)
                .startDate(LocalDateTime.of(2024, Month.FEBRUARY, 1, 12, 12, 12))
                .endDate(LocalDateTime.of(2024, Month.FEBRUARY, 2, 12, 12, 12))
                .item(itemOne)
                .status("APPROVED")
                .build();
        Booking bookingNext = Booking.builder()
                .booker(userOne)
                .startDate(LocalDateTime.of(2024, Month.APRIL, 1, 12, 12, 12))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 2, 12, 12, 12))
                .item(itemOne)
                .status("APPROVED")
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of());
        when(itemMapper.itemToDto(itemOne)).thenReturn(itemOneDto);
        when(bookingRepository
                .findTopByItemIdAndStartDateBeforeAndStatusOrderByEndDateDesc(any(), any(), any()))
                .thenReturn(bookingLast);
        when(bookingRepository
                .findTopByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(any(), any(), any()))
                .thenReturn(bookingNext);

        ItemDto response = itemService.getItem(1L, 1L);
        assertEquals(response, itemOneDto);
    }

    @Test
    void getItemThrowsObjectNotFoundExceptionTest() {
        when(itemRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Данный предмет не существет"));

        assertThrows(ObjectNotFoundException.class, () -> itemService.getItem(1L, 1L));
    }

    @Test
    void getItemsByUserTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .ownerId(1L)
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemRepository.findByOwnerIdOrderById(anyLong(), any())).thenReturn(new PageImpl<>(List.of(itemOne)));
        when(bookingRepository.findAll()).thenReturn(List.of());
        when(commentRepository.findAll()).thenReturn(List.of());
        when(itemMapper.itemToDto(itemOne)).thenReturn(itemOneDto);

        Collection<ItemDto> response = itemService.getItemsByUser(1L, 0, 10);
        assertEquals(response, List.of(itemOneDto));
    }

    @Test
    void searchItemsTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .ownerId(1L)
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        when(itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(anyString(),
                        anyString(), any())).thenReturn(new PageImpl<>(List.of(itemOne)));
        when(bookingRepository.findAll()).thenReturn(List.of());
        when(commentRepository.findAll()).thenReturn(List.of());
        when(itemMapper.itemToDto(itemOne)).thenReturn(itemOneDto);

        Collection<ItemDto> response = itemService.searchItems("description", 0, 10);
        assertEquals(response, List.of(itemOneDto));
    }

    @Test
    void searchItemsReturnsEmptyArrayTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .description("description item 1")
                .ownerId(1L)
                .available(true).build();
        ItemDto itemOneDto = ItemDto.builder()
                .name("item1")
                .description("description item 1")
                .available(true).build();

        Collection<ItemDto> response = itemService.searchItems("", 0, 10);
        assertEquals(response, List.of());
    }
}