package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql("/schema.sql")
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void findByOwnerIdTest() {
        User userOne = User.builder()
                .name("Carl")
                .email("carl@yandex.ru")
                .build();
        User userTwo = User.builder()
                .name("Carl2")
                .email("carl2@yandex.ru")
                .build();
        Item itemOne = Item.builder()
                .name("Book1")
                .ownerId(1L)
                .available(true)
                .description("Book1")
                .build();
        Item itemTwo = Item.builder()
                .name("Book2")
                .ownerId(2L)
                .available(true)
                .description("Book2")
                .build();

        userRepository.save(userTwo);
        userRepository.save(userOne);
        itemRepository.save(itemOne);
        itemRepository.save(itemTwo);

        Collection<Item> response = itemRepository.findByOwnerId(1L, Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 1);
        assert (response.contains(itemOne));
    }

    @Test
    void findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseTest() {
        User userOne = User.builder()
                .name("Carl")
                .email("carl@yandex.ru")
                .build();
        User userTwo = User.builder()
                .name("Carl2")
                .email("carl2@yandex.ru")
                .build();
        Item itemOne = Item.builder()
                .name("Book 1")
                .ownerId(1L)
                .available(true)
                .description("Book1")
                .build();
        Item itemTwo = Item.builder()
                .name("Book 2")
                .ownerId(2L)
                .available(true)
                .description("Book2")
                .build();

        userRepository.save(userTwo);
        userRepository.save(userOne);
        itemRepository.save(itemOne);
        itemRepository.save(itemTwo);

        Collection<Item> response = itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("book", "book",
                        Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 2);
        assert (response.contains(itemOne));
        assert (response.contains(itemTwo));
    }
}