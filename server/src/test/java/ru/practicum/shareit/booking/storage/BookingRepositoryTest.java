package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Sql("/schema.sql")
class BookingRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private User userOne;
    private User userTwo;
    private Item itemOne;
    private Item itemTwo;
    private Booking bookingOne;
    private Booking bookingTwo;
    private Booking bookingThree;
    private Booking bookingFour;


    @BeforeEach
    public void setUp() {
        userOne = User.builder()
                .name("Carl")
                .email("carl@yandex.ru")
                .build();
        userTwo = User.builder()
                .name("Carl2")
                .email("carl2@yandex.ru")
                .build();
        itemOne = Item.builder()
                .name("Book1")
                .ownerId(1L)
                .available(true)
                .description("Book1")
                .build();
        itemTwo = Item.builder()
                .name("Book2")
                .ownerId(2L)
                .available(true)
                .description("Book2")
                .build();
        bookingOne = Booking.builder()
                .booker(userOne)
                .item(itemOne)
                .startDate(LocalDateTime.of(2024, Month.APRIL, 21, 12, 12))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 21, 18, 12))
                .build();
        bookingTwo = Booking.builder()
                .booker(userOne)
                .item(itemTwo)
                .startDate(LocalDateTime.of(2024, Month.APRIL, 21, 12, 12))
                .endDate(LocalDateTime.of(2024, Month.MAY, 21, 18, 12))
                .build();
        bookingThree = Booking.builder()
                .booker(userTwo)
                .item(itemOne)
                .startDate(LocalDateTime.of(2024, Month.APRIL, 12, 12, 12))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 12, 18, 12))
                .build();
        bookingFour = Booking.builder()
                .booker(userTwo)
                .item(itemTwo)
                .startDate(LocalDateTime.of(2024, Month.APRIL, 14, 12, 12))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 16, 18, 12))
                .build();

        userRepository.save(userTwo);
        userRepository.save(userOne);
        itemRepository.save(itemOne);
        itemRepository.save(itemTwo);
        bookingRepository.save(bookingOne);
        bookingRepository.save(bookingTwo);
        bookingRepository.save(bookingThree);
        bookingRepository.save(bookingFour);
    }

    @Test
    void findByBookerIdOrderByStartDateDescTest() {
        Collection<Booking> response = bookingRepository.findByBookerIdOrderByStartDate(userOne.getId(),
                Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 2);
        assert (response.contains(bookingOne));
        assert (response.contains(bookingTwo));
    }

    @Test
    void findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDescTest() {
        LocalDateTime time = LocalDateTime.now();
        Collection<Booking> response = bookingRepository
                .findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userOne.getId(), time, time,
                        Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 0);
    }

    @Test
    void findByBookerIdAndEndDateBeforeOrderByStartDateDescTest() {
        LocalDateTime time = LocalDateTime.now();
        Collection<Booking> response = bookingRepository
                .findByBookerIdAndEndDateBeforeOrderByStartDateDesc(userOne.getId(), time,
                        Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 0);
    }

    @Test
    void findByBookerIdAndStartDateAfterOrderByStartDateDescTest() {
        LocalDateTime time = LocalDateTime.now();
        Collection<Booking> response = bookingRepository
                .findByBookerIdAndStartDateAfterOrderByStartDateDesc(userTwo.getId(), time,
                        Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 2);
        assert (response.contains(bookingThree));
        assert (response.contains(bookingFour));
    }

    @Test
    void findByBookerIdAndStatusEqualsOrderByStartDateDescTest() {
        Collection<Booking> response = bookingRepository
                .findByBookerIdAndStatusEqualsOrderByStartDateDesc(userOne.getId(), "APPROVED",
                        Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 0);
    }

    @Test
    void findByOwnerIdTest() {
        Collection<Booking> response = bookingRepository.findByOwnerId(userTwo.getId(),
                Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 2);
        assert (response.contains(bookingThree));
        assert (response.contains(bookingOne));
    }

    @Test
    void findByOwnerIdCurrentTest() {
        LocalDateTime time = LocalDateTime.now();
        Collection<Booking> response = bookingRepository.findByOwnerIdCurrent(userTwo.getId(), time, time,
                Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 0);
    }

    @Test
    void findByOwnerIdPastTest() {
        LocalDateTime time = LocalDateTime.now();
        Collection<Booking> response = bookingRepository.findByOwnerIdPast(userTwo.getId(), time,
                Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 0);
    }

    @Test
    void findByOwnerIdFutureTest() {
        LocalDateTime time = LocalDateTime.now();
        Collection<Booking> response = bookingRepository.findByOwnerIdFuture(userTwo.getId(), time,
                Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 2);
        assert (response.contains(bookingThree));
        assert (response.contains(bookingOne));
    }

    @Test
    void findByOwnerIdStatusTest() {
        Collection<Booking> response = bookingRepository.findByOwnerIdStatus(userTwo.getId(), "WAITING",
                Pageable.ofSize(10)).getContent();

        assertEquals(response.size(), 0);
    }

    @Test
    void findTopByItemIdAndStartDateBeforeAndStatusOrderByEndDateDescTest() {
        LocalDateTime time = LocalDateTime.now();
        Booking response = bookingRepository.findTopByItemIdAndStartDateBeforeAndStatusOrderByEndDateDesc(itemOne.getId(),
                time, "WAITING");

        assertNull(response);
    }

    @Test
    void findTopByItemIdAndStartDateAfterAndStatusOrderByStartDateAscTest() {
        LocalDateTime time = LocalDateTime.now();
        Booking response = bookingRepository.findTopByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(itemOne.getId(),
                time, "WAITING");

        assertNull(response);
    }

    @Test
    void findByItemIdAndBookerIdAndEndDateBeforeOrderByStartDateDescTest() {
        LocalDateTime time = LocalDateTime.now();
        Collection<Booking> response = bookingRepository.findByItemIdAndBookerIdAndEndDateBeforeOrderByStartDateDesc(itemOne.getId(),
                userOne.getId(), time);

        assertEquals(response.size(), 0);
    }
}