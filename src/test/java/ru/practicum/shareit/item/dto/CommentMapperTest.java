package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {
    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    void commentToDtoTest() {
        Comment commentOne = Comment.builder()
                .itemId(1L)
                .text("Super!")
                .build();
        CommentDto commentOneDto = CommentDto.builder()
                .text("Super!")
                .build();

        CommentDto response = commentMapper.commentToDto(commentOne);
        assertEquals(response, commentOneDto);
    }
}