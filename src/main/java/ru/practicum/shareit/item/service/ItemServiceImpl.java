package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbiddenError;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           BookingRepository bookingRepository, ItemMapper itemMapper,
                           CommentRepository commentRepository, CommentMapper commentMapper) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.itemMapper = itemMapper;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        Item item = itemMapper.dtoToItem(itemDto);
        item.setOwnerId(userId);
        return itemMapper.itemToDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        if (userId == null) {
            throw new InvalidRequestException("Не указан id пользователя");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Данный предмет не существет"));
        if (!Objects.equals(item.getOwnerId(), userId)) {
            throw new AccessForbiddenError("У данного пользователя нет доступа к редактированию этого предмета.");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemMapper.itemToDto(itemRepository.save(item));
    }


    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Данный предмет не существет"));
        Long currentUserId = item.getOwnerId();
        ItemDto itemDto = itemMapper.itemToDto(item);
        if (Objects.equals(currentUserId, userId)) {
            itemDto = addLastNextBookingsForOneItem(itemDto);
        }
        itemDto.setComments(commentRepository
                .findByItemId(itemId).stream().map(commentMapper::commentToDto).collect(Collectors.toList()));
        return itemDto;
    }

    @Override
    public Collection<ItemDto> getItemsByUser(Long userId) {
        Collection<Item> items = itemRepository.findByOwnerId(userId);
        return addLastNextBookingsForItems(items);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (Objects.equals(text, "")) {
            return new ArrayList<>();
        }
        return addLastNextBookingsForItems(itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text).stream()
                .filter(Item::getAvailable).collect(Collectors.toList()));
    }

    private ItemDto addLastNextBookingsForOneItem(ItemDto itemDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        Booking lastBooking = bookingRepository
                .findTopByItemIdAndStartDateBeforeAndStatusOrderByEndDateDesc(itemDto.getId(),
                        currentTime, "APPROVED");
        if (lastBooking != null) {
            itemDto.setLastBooking(BookingDtoShort.builder()
                    .id(lastBooking.getId())
                    .bookerId(lastBooking.getBooker().getId())
                    .build());
        }
        Booking nextBooking = bookingRepository
                .findTopByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(itemDto.getId(),
                        currentTime, "APPROVED");
        if (nextBooking != null) {
            itemDto.setNextBooking(BookingDtoShort.builder()
                    .id(nextBooking.getId())
                    .bookerId(nextBooking.getBooker().getId())
                    .build());
        }
        return itemDto;
    }

    private Collection<ItemDto> addLastNextBookingsForItems(Collection<Item> items) {
        LocalDateTime currentTime = LocalDateTime.now();
        Map<Long, List<Booking>> bookings = bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(o -> o.getItem().getId(),
                        Collectors.mapping(o -> o, Collectors.toList())));
        Map<Long, List<Comment>> comments = commentRepository.findAll().stream()
                .collect(Collectors.groupingBy(Comment::getItemId,
                        Collectors.mapping(o -> o, Collectors.toList())));

        Collection<ItemDto> response = new ArrayList<>();

        for (Item currentItem : items) {
            ItemDto currentItemDto = itemMapper.itemToDto(currentItem);
            List<Booking> itemBookings = bookings.getOrDefault(currentItemDto.getId(), List.of());
            if (itemBookings.size() > 0) {
                itemBookings.sort(Comparator.comparingInt(o -> o.getStartDate().getSecond()));
            }
            Booking lastBooking = null;
            Booking nextBooking = null;
            for (Booking booking : itemBookings) {
                if (booking.getStartDate().isAfter(currentTime)) {
                    nextBooking = booking;
                } else {
                    lastBooking = booking;
                    break;
                }
            }
            if (lastBooking != null) {
                currentItemDto.setLastBooking(BookingDtoShort.builder()
                        .id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId())
                        .build());
            }
            if (nextBooking != null) {
                currentItemDto.setNextBooking(BookingDtoShort.builder()
                        .id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId())
                        .build());
            }
            currentItemDto.setComments(comments.getOrDefault(currentItemDto.getId(), List.of())
                    .stream().map(commentMapper::commentToDto).collect(Collectors.toList()));
            response.add(currentItemDto);
        }
        return response;
    }
}
