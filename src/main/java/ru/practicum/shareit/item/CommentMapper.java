package ru.practicum.shareit.item;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Mapper
@AnnotateWith(value = Component.class)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    Comment map(String text, Item item, User author, LocalDateTime created);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto map(Comment comment);
}
