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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор вещи

    @NotBlank
    @Column(length = 255)
    private String name; // краткое название

    @NotBlank
    @Column(length = 1024)
    private String description; // развёрнутое описание

    @NotNull
    @Column(name = "is_available")
    private Boolean available; // статус о том, доступна или нет вещь для аренды

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // владелец вещи

    // если вещь была создана по запросу другого пользователя,
    // то в этом поле будет храниться ссылка на соответствующий запрос
//    @ManyToOne
//    @JoinColumn(name = "request_id")
//    private ItemRequest request;
}
