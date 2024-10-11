package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long ownerId);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start <= ?2 and b.end >= ?2")
    List<Booking> findByBookerIdCurrentAndSort(long bookerId, LocalDateTime date, Sort sort);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <= ?2 and b.end >= ?2")
    List<Booking> findByItemOwnerIdCurrentAndSort(long ownerId, LocalDateTime date, Sort sort);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime date);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime date);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime date);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime date);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);

    List<Booking> findByItemIdInAndStartAfterOrderByStart(List<Long> itemIds, LocalDateTime now);

    List<Booking> findByItemIdAndStartAfterOrderByStart(long itemId, LocalDateTime now);

    boolean existsByBookerIdAndItemIdAndStartLessThanEqual(long bookerId, long itemId, LocalDateTime date);
}
