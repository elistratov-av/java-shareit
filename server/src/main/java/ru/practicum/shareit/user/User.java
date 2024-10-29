package ru.practicum.shareit.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор пользователя

    @Column(length = 255)
    private String name; // имя или логин пользователя

    // адрес электронной почты
    // (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты)
    @Column(length = 512, unique = true)
    private String email;
}
