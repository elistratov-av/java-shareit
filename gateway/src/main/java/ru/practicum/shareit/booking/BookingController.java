package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.util.Headers;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Headers.USER_ID) long userId, @RequestBody @Valid BookingCreateDto newBooking) {
        log.info("==> create booking: {}", newBooking);
        ResponseEntity<Object> booking = bookingClient.bookItem(userId, newBooking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        log.info("==> approve bookingId = {}, ownerId = {}, approved = {}", bookingId, userId, approved);
        ResponseEntity<Object> booking = bookingClient.approve(userId, bookingId, approved);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long bookingId) {
        log.info("==> get bookingId = {}, ownerId = {}", bookingId, userId);
        ResponseEntity<Object> booking = bookingClient.get(userId, bookingId);
        return booking;
    }

    @GetMapping
    public ResponseEntity<Object> findByBooker(@RequestHeader(Headers.USER_ID) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("==> findByBooker: {}, userId = {}", state, userId);
        ResponseEntity<Object> bookings = bookingClient.findByBooker(userId, state);
        return bookings;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findByOwner(@RequestHeader(Headers.USER_ID) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("==> findByOwner: {}, userId = {}", state, userId);
        ResponseEntity<Object> bookings = bookingClient.findByOwner(userId, state);
        return bookings;
    }
}
