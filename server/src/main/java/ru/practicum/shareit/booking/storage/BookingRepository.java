package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select b.* from bookings as b where b.booker_id = ?1 order " +
            "by b.start_date desc", nativeQuery = true)
    Page<Booking> findByBookerIdOrderByStartDate(Long userId, Pageable pageable);

    Page<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long userId,
                                                                                      LocalDateTime current,
                                                                                      LocalDateTime currentAnother,
                                                                                      Pageable pageable);

    Page<Booking> findByBookerIdAndEndDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime current,
                                                                     Pageable pageable);

    Page<Booking> findByBookerIdAndStartDateAfterOrderByStartDateDesc(Long userId, LocalDateTime current,
                                                                      Pageable pageable);

    Page<Booking> findByBookerIdAndStatusEqualsOrderByStartDateDesc(Long userId, String status, Pageable pageable);

    @Query(value = "select b.* from items as it join bookings as b on b.item_id = it.id where it.owner_id = ?1 order " +
            "by b.start_date desc", nativeQuery = true)
    Page<Booking> findByOwnerId(Long userId, Pageable pageable);

    @Query(value = "select distinct b.* from items as it join bookings as b on b.item_id = it.id where " +
            "it.owner_id = ?1 and b.start_date < ?2 and b.end_date > ?3 order by b.start_date desc", nativeQuery = true)
    Page<Booking> findByOwnerIdCurrent(Long userId, LocalDateTime current, LocalDateTime currentAnother,
                                       Pageable pageable);

    @Query(value = "select distinct b.* from items as it join bookings as b on b.item_id = it.id where it.owner_id = ?1 and " +
            "b.end_date < ?2 order by b.start_date desc", nativeQuery = true)
    Page<Booking> findByOwnerIdPast(Long userId, LocalDateTime current, Pageable pageable);

    @Query(value = "select distinct b.* from items as it join bookings as b on b.item_id = it.id where it.owner_id = ?1 and " +
            "b.start_date > ?2 order by b.start_date desc", nativeQuery = true)
    Page<Booking> findByOwnerIdFuture(Long userId, LocalDateTime current, Pageable pageable);

    @Query(value = "select b.* from items as it LEFT JOIN  bookings as b on b.item_id = it.id where " +
            "it.owner_id = ?1 and b.owner_approval = ?2 order by b.start_date desc", nativeQuery = true)
    Page<Booking> findByOwnerIdStatus(Long userId, String status, Pageable pageable);

    Booking findTopByItemIdAndStartDateBeforeAndStatusOrderByEndDateDesc(Long itemId, LocalDateTime current,
                                                                         String status);

    Booking findTopByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(Long itemId, LocalDateTime current,
                                                                         String status);

    Collection<Booking> findByItemIdAndBookerIdAndEndDateBeforeOrderByStartDateDesc(Long itemId, Long bookerId,
                                                                                    LocalDateTime current);
}
