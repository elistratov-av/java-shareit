package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.PracticumTestContext;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BookingServiceTest extends PracticumTestContext {
    private final EntityManager em;
    private final BookingService bookingService;

    @Test
    void testGet() {
        long userId = 1L;
        long bookingId = 1L;

        BookingDto booking = bookingService.get(bookingId, userId);

        assertEquals(booking1, booking);
    }

    @Test
    void testGetByOwner() {
        long userId = 1L;
        long bookingId = 2L;

        BookingDto booking = bookingService.get(bookingId, userId);

        assertEquals(booking2, booking);
    }

    @Test
    void testGetUserNotFound() {
        long userId = 4L;
        long bookingId = 1L;

        assertThrows(
                NotFoundException.class,
                () -> bookingService.get(bookingId, userId)
        );
    }

    @Test
    void testGetBookingNotFound() {
        long userId = 1L;
        long bookingId = 4L;

        assertThrows(
                NotFoundException.class,
                () -> bookingService.get(bookingId, userId)
        );
    }

    @Test
    void testGetNotAuthor() {
        long userId = 2L;
        long bookingId = 1L;

        assertThrows(
                ConditionsNotMetException.class,
                () -> bookingService.get(bookingId, userId)
        );
    }

    @Test
    void testAdd() {
        long userId = 1L;
        long itemId = 2L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        BookingCreateDto newBooking = new BookingCreateDto(itemId, start, end);

        BookingDto bookingDto = bookingService.add(newBooking, userId);
        assertThat(bookingDto.getId(), notNullValue());
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking actualBooking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertThat(actualBooking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(actualBooking.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(actualBooking.getItem().getId(), equalTo(bookingDto.getItem().getId()));
        assertThat(actualBooking.getBooker().getId(), equalTo(bookingDto.getBooker().getId()));
        assertThat(actualBooking.getStatus(), equalTo(bookingDto.getStatus()));
    }

    @Test
    void testAddNotAvailable() {
        long userId = 1L;
        long itemId = 3L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        BookingCreateDto newBooking = new BookingCreateDto(itemId, start, end);

        assertThrows(
                ConditionsNotMetException.class,
                () -> bookingService.add(newBooking, userId)
        );
    }

    @Test
    void testApprove() {
        long bookingId = 2L;
        long userId = 1L;
        BookingApproveDto bookingApprove = new BookingApproveDto(bookingId, userId, true);

        BookingDto bookingDto = bookingService.approve(bookingApprove);
        assertThat(bookingDto.getId(), notNullValue());
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking actualBooking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertEquals(BookingStatus.APPROVED, actualBooking.getStatus());
    }

    @Test
    void testReject() {
        long bookingId = 2L;
        long userId = 1L;
        BookingApproveDto bookingApprove = new BookingApproveDto(bookingId, userId, false);

        BookingDto bookingDto = bookingService.approve(bookingApprove);
        assertThat(bookingDto.getId(), notNullValue());
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking actualBooking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertEquals(BookingStatus.REJECTED, actualBooking.getStatus());
    }

    @Test
    void testApproveBookingNotFound() {
        long bookingId = 4L;
        long userId = 2L;
        BookingApproveDto bookingApprove = new BookingApproveDto(bookingId, userId, true);

        assertThrows(
                NotFoundException.class,
                () -> bookingService.approve(bookingApprove)
        );
    }

    @Test
    void testApproveNotOwner() {
        long bookingId = 2L;
        long userId = 2L;
        BookingApproveDto bookingApprove = new BookingApproveDto(bookingId, userId, true);

        assertThrows(
                ForbiddenException.class,
                () -> bookingService.approve(bookingApprove)
        );
    }

    @Test
    void testFindByBookerAll() {
        long userId = 1L;
        BookingState state = BookingState.ALL;

        List<BookingDto> bookings = bookingService.findByBooker(userId, state);

        assertEquals(List.of(booking1), bookings);
    }

    @Test
    void testFindByBookerUserNotFound() {
        long userId = 4L;
        BookingState state = BookingState.ALL;

        assertThrows(
                NotFoundException.class,
                () -> bookingService.findByBooker(userId, state)
        );
    }

    @Test
    void testFindByBookerCurrent() {
        long userId = 2L;
        BookingState state = BookingState.CURRENT;

        List<BookingDto> bookings = bookingService.findByBooker(userId, state);

        assertEquals(List.of(booking2), bookings);
    }

    @Test
    void testFindByBookerPast() {
        long userId = 1L;
        BookingState state = BookingState.PAST;

        List<BookingDto> bookings = bookingService.findByBooker(userId, state);

        assertEquals(List.of(booking1), bookings);
    }

    @Test
    void testFindByBookerFuture() {
        long userId = 2L;
        BookingState state = BookingState.FUTURE;

        List<BookingDto> bookings = bookingService.findByBooker(userId, state);

        assertEquals(List.of(booking3), bookings);
    }

    @Test
    void testFindByBookerWaiting() {
        long userId = 1L;
        BookingState state = BookingState.WAITING;

        List<BookingDto> bookings = bookingService.findByBooker(userId, state);

        assertEquals(List.of(booking1), bookings);
    }

    @Test
    void testFindByBookerRejected() {
        long userId = 2L;
        BookingState state = BookingState.REJECTED;

        List<BookingDto> bookings = bookingService.findByBooker(userId, state);

        assertEquals(List.of(booking3), bookings);
    }

    @Test
    void testFindByOwnerAll() {
        long userId = 1L;
        BookingState state = BookingState.ALL;

        List<BookingDto> bookings = bookingService.findByOwner(userId, state);

        assertEquals(List.of(booking3, booking1, booking2), bookings);
    }

    @Test
    void testFindByOwnerUserNotFound() {
        long userId = 4L;
        BookingState state = BookingState.ALL;

        assertThrows(
                NotFoundException.class,
                () -> bookingService.findByOwner(userId, state)
        );
    }

    @Test
    void testFindByOwnerCurrent() {
        long userId = 1L;
        BookingState state = BookingState.CURRENT;

        List<BookingDto> bookings = bookingService.findByOwner(userId, state);

        assertEquals(List.of(booking2), bookings);
    }

    @Test
    void testFindByOwnerPast() {
        long userId = 1L;
        BookingState state = BookingState.PAST;

        List<BookingDto> bookings = bookingService.findByOwner(userId, state);

        assertEquals(List.of(booking1), bookings);
    }

    @Test
    void testFindByOwnerFuture() {
        long userId = 1L;
        BookingState state = BookingState.FUTURE;

        List<BookingDto> bookings = bookingService.findByOwner(userId, state);

        assertEquals(List.of(booking3), bookings);
    }

    @Test
    void testFindByOwnerWaiting() {
        long userId = 1L;
        BookingState state = BookingState.WAITING;

        List<BookingDto> bookings = bookingService.findByOwner(userId, state);

        assertEquals(List.of(booking1, booking2), bookings);
    }

    @Test
    void testFindByOwnerRejected() {
        long userId = 1L;
        BookingState state = BookingState.REJECTED;

        List<BookingDto> bookings = bookingService.findByOwner(userId, state);

        assertEquals(List.of(booking3), bookings);
    }
}
