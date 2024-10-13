package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.util.Headers;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(Headers.USER_ID) long userId, @Valid @RequestBody BookingCreateDto newBooking) {
        log.info("==> create booking: {}", newBooking);
        BookingDto booking = bookingService.add(newBooking, userId);
        log.info("<== create booking: {}", booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        log.info("==> approve bookingId = {}, ownerId = {}, approved = {}", bookingId, userId, approved);
        BookingApproveDto bookingApprove = new BookingApproveDto(bookingId, userId, approved);
        BookingDto booking = bookingService.approve(bookingApprove);
        log.info("<== approve booking: {}", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long bookingId) {
        log.info("==> get bookingId = {}, ownerId = {}", bookingId, userId);
        BookingDto booking = bookingService.get(bookingId, userId);
        log.info("<== get booking: {}, ownerId = {}", booking, userId);
        return booking;
    }

    @GetMapping
    public List<BookingDto> findByBooker(@RequestHeader(Headers.USER_ID) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("==> findByBooker: {}, userId = {}", state, userId);
        List<BookingDto> bookings = bookingService.findByBooker(userId, state);
        log.info("<== findByBooker: {} - {}, userId = {}", state, bookings.size(), userId);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDto> findByOwner(@RequestHeader(Headers.USER_ID) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("==> findByOwner: {}, userId = {}", state, userId);
        List<BookingDto> bookings = bookingService.findByOwner(userId, state);
        log.info("<== findByOwner: {} - {}, userId = {}", state, bookings.size(), userId);
        return bookings;
    }
}
