package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql("/schema.sql")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void findByEmailTest() {
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

        userRepository.save(userOne);
        userRepository.save(userTwo);

        User response = userRepository.findByEmail(userOne.getEmail());

        assertEquals(response.getId(), userOne.getId());
        assertEquals(response.getName(), userOne.getName());
        assertEquals(response.getEmail(), userOne.getEmail());
    }
}