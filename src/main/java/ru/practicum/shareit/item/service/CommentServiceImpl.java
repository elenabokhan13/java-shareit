package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, BookingRepository bookingRepository,
                              CommentRepository commentRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.commentMapper = commentMapper;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public CommentDto postComment(Long userId, Long itemId, CommentDto commentDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Данный предмет не существет"));
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Данный пользователь не существет"));
        Collection<Booking> bookings = bookingRepository
                .findByItemIdAndBookerIdAndEndDateBeforeOrderByStartDateDesc(itemId, userId, currentTime);
        if (bookings.size() > 0) {
            comment.setItemId(itemId);
            comment.setAuthorName(currentUser.getName());
            comment.setCreated(currentTime);
            return commentMapper.commentToDto(commentRepository.save(comment));
        } else {
            throw new InvalidRequestException("У пользователя нет прав на размещение комментария");
        }
    }
}
