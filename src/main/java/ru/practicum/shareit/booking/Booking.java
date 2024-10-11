package ru.practicum.shareit.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор бронирования

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start; // дата и время начала бронирования

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end; // дата и время конца бронирования

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; // вещь, которую пользователь бронирует

    @NotNull
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker; // пользователь, который осуществляет бронирование

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // статус бронирования
}
