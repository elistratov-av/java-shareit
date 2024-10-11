package ru.practicum.shareit.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор комментария

    @NotBlank
    @Column(length = 1024)
    private String text; // текст комментария

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; // вещь, которую пользователь комментирует

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author; // пользователь, написавший комментарий

    @NotNull
    @Column(name = "created", nullable = false)
    private LocalDateTime created; // дата создания комментария
}
