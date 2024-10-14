package ru.practicum.shareit.booking.impl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto get(long id, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + id + " не найдено"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId)
            throw new ConditionsNotMetException("Может быть выполнено либо автором бронирования, " +
                    "либо владельцем вещи, к которой относится бронирование");
        return bookingMapper.map(booking);
    }

    @Override
    public BookingDto add(BookingCreateDto newBooking, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        Item item = itemRepository.findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + newBooking.getItemId() + " не найдена"));
        if (!item.getAvailable())
            throw new ValidationException("Вещь недоступна для бронирования");

        Booking booking = bookingRepository.save(bookingMapper.map(newBooking, item, owner, BookingStatus.WAITING));
        return bookingMapper.map(booking);
    }

    @Override
    public BookingDto approve(BookingApproveDto bookingApprove) {
        Booking booking = bookingRepository.findById(bookingApprove.getId())
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + bookingApprove.getId() + " не найдено"));
        if (booking.getItem().getOwner().getId() != bookingApprove.getUserId())
            throw new ForbiddenException("Может быть выполнено только владельцем вещи");

        booking.setStatus(bookingApprove.isApproved() ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);
        return bookingMapper.map(booking);
    }

    private List<Booking> findByBookerIdAndState(long userId, BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state) {
            case ALL:
                return bookingRepository.findByBookerIdOrderByStartDesc(userId);
            case CURRENT:
                return bookingRepository.findByBookerIdCurrentAndSort(userId, now, sort);
            case PAST:
                return bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE:
                return bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING:
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default:
                throw new UnsupportedOperationException("Выборка бронирований для состояния " + state + " не поддерживается");
        }
    }

    private List<Booking> findByOwnerIdAndState(long userId, BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state) {
            case ALL:
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT:
                return bookingRepository.findByItemOwnerIdCurrentAndSort(userId, now, sort);
            case PAST:
                return bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE:
                return bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING:
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default:
                throw new UnsupportedOperationException("Выборка бронирований для состояния " + state + " не поддерживается");
        }
    }

    @Override
    public List<BookingDto> findByBooker(long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        List<Booking> bookings = findByBookerIdAndState(userId, state);
        return bookingMapper.map(bookings);
    }

    @Override
    public List<BookingDto> findByOwner(long ownerId, BookingState state) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + ownerId + " не найден"));

        List<Booking> bookings = findByOwnerIdAndState(ownerId, state);
        return bookingMapper.map(bookings);
    }
}
