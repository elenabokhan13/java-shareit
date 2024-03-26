package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql("/schema.sql")
class CommentRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private User userOne;
    private Item itemOne;
    private Comment commentOne;
    private Comment commentTwo;

    @BeforeEach
    public void setUp() {
        userOne = User.builder()
                .name("Carl")
                .email("carl@yandex.ru")
                .build();
        itemOne = Item.builder()
                .name("Book1")
                .ownerId(1L)
                .available(true)
                .description("Book1")
                .build();
        commentOne = Comment.builder()
                .itemId(1L)
                .text("Comment1")
                .build();
        commentTwo = Comment.builder()
                .itemId(1L)
                .text("Comment2")
                .build();
        userRepository.save(userOne);
        itemRepository.save(itemOne);
        commentRepository.save(commentOne);
        commentRepository.save(commentTwo);
    }


    @Test
    void findByItemId() {
        List<Comment> response = commentRepository.findByItemId(1L);

        assertEquals(response.size(), 2);
        assert response.contains(commentOne);
        assert response.contains(commentTwo);
    }
}