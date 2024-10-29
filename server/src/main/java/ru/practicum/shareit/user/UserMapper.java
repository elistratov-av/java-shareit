package ru.practicum.shareit.user;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper
@AnnotateWith(value = Component.class)
public interface UserMapper {
    UserDto toUserDto(User item);

    User toUser(UserDto item);
}
