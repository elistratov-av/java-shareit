package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemInfoDto {
    private Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private final Long requestId;
    private final BookingDto nextBooking;
    private final BookingDto lastBooking;
    private final List<CommentDto> comments;
}
