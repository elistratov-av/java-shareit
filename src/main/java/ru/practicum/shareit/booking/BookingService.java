package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto get(long id, long userId);

    BookingDto add(BookingCreateDto newBooking, long userId);

    BookingDto approve(BookingApproveDto booking);

    List<BookingDto> findByBooker(long userId, BookingState state);

    List<BookingDto> findByOwner(long ownerId, BookingState state);
}
