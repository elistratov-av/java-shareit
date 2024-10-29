package ru.practicum.shareit.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор запроса

    @Column(length = 1024)
    private String description; // текст запроса, содержащий описание требуемой вещи

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor; // пользователь, создавший запрос

    @Column(name = "created")
    private LocalDateTime created; // дата и время создания запроса
}
