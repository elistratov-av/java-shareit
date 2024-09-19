package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "requestId", source = "request.id")
    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtoList(List<Item> items);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    Item toItem(ItemDto item, User owner, ItemRequest request);
}
