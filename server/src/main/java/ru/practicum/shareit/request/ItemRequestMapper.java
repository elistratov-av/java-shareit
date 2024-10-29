package ru.practicum.shareit.request;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(uses = ItemMapper.class)
@AnnotateWith(value = Component.class)
public interface ItemRequestMapper {
    ItemRequestDto map(ItemRequest itemRequest);

    List<ItemRequestDto> map(List<ItemRequest> requests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestor", source = "requestor")
    @Mapping(target = "created", source = "created")
    ItemRequest map(ItemRequestCreateDto newItemRequest, User requestor, LocalDateTime created);

    ItemRequestInfoDto toItemRequestInfoDto(ItemRequest request, List<Item> items);
}
