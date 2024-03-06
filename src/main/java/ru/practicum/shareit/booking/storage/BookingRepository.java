package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDateDesc(Long userId);

    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long userId,
                                                                                      LocalDateTime current,
                                                                                      LocalDateTime currentAnother);

    List<Booking> findByBookerIdAndEndDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime current);

    List<Booking> findByBookerIdAndStartDateAfterOrderByStartDateDesc(Long userId, LocalDateTime current);

    List<Booking> findByBookerIdAndStatusEqualsOrderByStartDateDesc(Long userId, String status);

    @Query(value = "select distinct b.* from items as it join bookings as b where it.owner_id = ?1 order " +
            "by start_date desc", nativeQuery = true)
    Collection<Booking> findByOwnerId(Long userId);

    @Query(value = "select distinct b.* from items as it join bookings as b on b.ITEM_ID = it.ID where " +
            "it.owner_id = ?1 and b.start_date < ?2 and b.end_date > ?3 order by start_date desc", nativeQuery = true)
    Collection<Booking> findByOwnerIdCurrent(Long userId, LocalDateTime current, LocalDateTime currentAnother);

    @Query(value = "select distinct b.* from items as it join bookings as b where it.owner_id = ?1 and " +
            "b.end_date < ?2 order by start_date desc", nativeQuery = true)
    Collection<Booking> findByOwnerIdPast(Long userId, LocalDateTime current);

    @Query(value = "select distinct b.* from items as it join bookings as b where it.owner_id = ?1 and " +
            "b.start_date > ?2 order by start_date desc", nativeQuery = true)
    Collection<Booking> findByOwnerIdFuture(Long userId, LocalDateTime current);

    @Query(value = "select b.* from items as it LEFT JOIN  bookings as b  on b.item_id = it.id where " +
            "it.owner_id = ?1 AND b.OWNER_APPROVAL = ?2 order by start_date DESC", nativeQuery = true)
    Collection<Booking> findByOwnerIdStatus(Long userId, String status);

    Booking findTopByItemIdAndStartDateBeforeAndStatusOrderByEndDateDesc(Long itemId, LocalDateTime current,
                                                                         String status);

    Booking findTopByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(Long itemId, LocalDateTime current,
                                                                         String status);

    Collection<Booking> findByItemIdAndBookerIdAndEndDateBeforeOrderByStartDateDesc(Long itemId, Long bookerId,
                                                                                    LocalDateTime current);
}
