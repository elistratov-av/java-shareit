package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreateDto {
    @NotNull
    private Long itemId;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start; // дата и время начала бронирования
    @NotNull
    @Future
    private LocalDateTime end; // дата и время конца бронирования

    @AssertTrue(message = "Дата начала должна быть до даты окончания бронирования")
    public boolean isStartBeforeEnd() {
        if (start == null || end == null) return false;
        return start.isBefore(end);
    }
}
