package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingApproveDto {
    private long id;
    private long userId;
    private boolean approved;
}
