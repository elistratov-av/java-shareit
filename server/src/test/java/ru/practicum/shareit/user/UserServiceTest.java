package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.PracticumTestContext;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceTest extends PracticumTestContext {
    private final EntityManager em;
    private final UserService userService;

    @Test
    void testGet() {
        long userId = 1L;

        UserDto user = userService.get(userId);

        assertNotNull(user);
        assertEquals(user1, user);
    }

    @Test
    void testAdd() {
        UserDto newUser = new UserDto(null, "new name", "newemail@ya.ru");

        UserDto userDto = userService.add(newUser);
        assertThat(userDto.getId(), notNullValue());
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        User actualUser = query.setParameter("id", userDto.getId())
                .getSingleResult();

        assertThat(actualUser.getName(), equalTo(userDto.getName()));
        assertThat(actualUser.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void testAddDuplicated() {
        UserDto newUser = new UserDto(null, "new name", "email1@yandex.ru");

        assertThrows(
                DuplicatedDataException.class,
                () -> userService.add(newUser)
        );
    }

    @Test
    void testUpdate() {
        UserDto newUser = new UserDto(1L, "new name", "newemail@ya.ru");

        UserDto userDto = userService.update(newUser);

        assertThat(newUser, equalTo(userDto));
    }

    @Test
    void testDelete() {
        long userId = 3L;

        userService.delete(userId);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        assertThrows(NoResultException.class, () -> query.setParameter("id", userId)
                .getSingleResult());
    }

}
