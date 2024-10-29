package ru.practicum.shareit;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

public class PracticumTestContext {

    public static final ItemDto item1 = new ItemDto(1L,
            "name1",
            "description1",
            true,
            null);
    public static final ItemDto item2 = new ItemDto(2L,
            "name2",
            "description2",
            true,
            null);
    public static final ItemDto item3 = new ItemDto(3L,
            "name3",
            "description3",
            false,
            1L);

    public static final UserDto user1 = new UserDto(1L,
            "name1",
            "email1@yandex.ru");
    public static final UserDto user2 = new UserDto(2L,
            "name1",
            "email1@yandex.ru");
    public static final UserDto user3 = new UserDto(3L,
            "name1",
            "email1@yandex.ru");

    public static final BookingDto booking1 = new BookingDto(1L,
            LocalDateTime.of(2024, 10, 26, 18, 30, 5),
            LocalDateTime.of(2024, 10, 27, 18, 30, 5),
            item1,
            user1,
            BookingStatus.WAITING);
    public static final BookingDto booking2 = new BookingDto(2L,
            LocalDateTime.of(2024, 10, 26, 18, 30, 5),
            LocalDateTime.of(2024, 11, 27, 18, 30, 5),
            item1,
            user3,
            BookingStatus.WAITING);
    public static final BookingDto booking3 = new BookingDto(3L,
            LocalDateTime.of(2024, 11, 15, 18, 30, 5),
            LocalDateTime.of(2024, 11, 27, 18, 30, 5),
            item1,
            user2,
            BookingStatus.REJECTED);

    public static final ItemRequestDto request1 = new ItemRequestDto(1L,
            "description1",
            LocalDateTime.of(2024, 10, 26, 18, 30, 5));
    public static final ItemRequestDto request2 = new ItemRequestDto(2L,
            "description1",
            LocalDateTime.of(2024, 10, 26, 18, 30, 5));

    public static final CommentDto comment1 = new CommentDto(1L,
            "text1",
            "name1",
            LocalDateTime.of(2024, 10, 26, 18, 30, 5));
    public static final CommentDto comment2 = new CommentDto(2L,
            "text1",
            "name1",
            LocalDateTime.of(2024, 10, 26, 18, 30, 5));
    public static final CommentDto comment3 = new CommentDto(3L,
            "text1",
            "name1",
            LocalDateTime.of(2024, 10, 26, 18, 30, 5));

}
