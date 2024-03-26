package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql("/schema.sql")
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void getByUserIdOrderByCreatedAsc() {
        User userOne = User.builder()
                .id(1L)
                .name("Carl")
                .email("carl@yandex.ru")
                .build();
        User userTwo = User.builder()
                .id(2L)
                .name("Carl2")
                .email("carl2@yandex.ru")
                .build();
        ItemRequest itemRequestOne = ItemRequest.builder()
                .userId(1L)
                .description("Нужен телефон")
                .created(LocalDateTime.of(2024, Month.MARCH, 30, 12, 00))
                .build();
        ItemRequest itemRequestTwo = ItemRequest.builder()
                .userId(2L)
                .description("Нужен телефон2")
                .created(LocalDateTime.of(2024, Month.MARCH, 30, 14, 00))
                .build();

        userRepository.save(userOne);
        userRepository.save(userTwo);
        itemRequestRepository.save(itemRequestOne);
        itemRequestRepository.save(itemRequestTwo);

        Collection<ItemRequest> response = itemRequestRepository.getByUserIdOrderByCreatedAsc(1L);

        assertEquals(response.size(), 1);
        assert (response.contains(itemRequestOne));
    }

    @Test
    void findAllByOrderByCreatedAsc() {
        User userOne = User.builder()
                .id(1L)
                .name("Carl")
                .email("carl@yandex.ru")
                .build();
        User userTwo = User.builder()
                .id(2L)
                .name("Carl2")
                .email("carl2@yandex.ru")
                .build();
        ItemRequest itemRequestOne = ItemRequest.builder()
                .userId(1L)
                .description("Нужен телефон")
                .created(LocalDateTime.of(2024, Month.MARCH, 30, 12, 00))
                .build();
        ItemRequest itemRequestTwo = ItemRequest.builder()
                .userId(2L)
                .description("Нужен телефон2")
                .created(LocalDateTime.of(2024, Month.MARCH, 30, 14, 00))
                .build();

        userRepository.save(userOne);
        userRepository.save(userTwo);
        itemRequestRepository.save(itemRequestOne);
        itemRequestRepository.save(itemRequestTwo);

        Collection<ItemRequest> response = itemRequestRepository
                .findAllByOrderByCreatedAsc(Pageable.ofSize(10)).getContent();
        assertEquals(response.size(), 2);
        assert (response.contains(itemRequestOne));
        assert (response.contains(itemRequestTwo));
    }
}