package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    private CommentService commentService;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        commentService = new CommentServiceImpl(commentMapper, bookingRepository, commentRepository, userRepository,
                itemRepository);
    }

    @Test
    void addCommentTest() {
        User userOne = User.builder()
                .name("User1")
                .email("user1@user.om")
                .build();
        Comment commentOne = Comment.builder()
                .itemId(1L)
                .text("Super!")
                .build();
        CommentDto commentOneDto = CommentDto.builder()
                .text("Super!")
                .build();
        Booking booking = Booking.builder()
                .booker(userOne)
                .build();

        when(commentMapper.commentDtoToComment(any())).thenReturn(commentOne);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userOne));
        when(commentMapper.commentToDto(any())).thenReturn(commentOneDto);
        when(bookingRepository.findByItemIdAndBookerIdAndEndDateBeforeOrderByStartDateDesc(anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking));

        CommentDto response = commentService.addComment(1L, 1L, commentOneDto);

        assertEquals(response.getText(), "Super!");
    }

    @Test
    void addCommentThrowsObjectNotFoundExceptionTest() {
        Comment commentOne = Comment.builder()
                .itemId(1L)
                .text("Super!")
                .build();
        CommentDto commentOneDto = CommentDto.builder()
                .text("Super!")
                .build();

        when(commentMapper.commentDtoToComment(any())).thenReturn(commentOne);
        when(itemRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> commentService.addComment(1L, 1L, commentOneDto));
    }
}