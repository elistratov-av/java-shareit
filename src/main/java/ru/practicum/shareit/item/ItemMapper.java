package ru.practicum.shareit.item;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper
@AnnotateWith(value = Component.class)
public interface ItemMapper {
    ItemDto toItemDto(Item item);

    @Mapping(target = "id", source = "item.id")
    ItemInfoDto toItemInfoDto(Item item, Booking nextBooking, Booking lastBooking, List<Comment> comments);

    default ItemInfoDto toItemInfoDto(Item item, List<Booking> bookings, List<Comment> comments) {
        if (bookings == null || bookings.isEmpty())
            return toItemInfoDto(item, null, null, comments);
        return toItemInfoDto(item, bookings.getFirst(), bookings.getLast(), comments);
    }

    List<ItemDto> toItemDtoList(List<Item> items);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    Item toItem(ItemCreateDto item, User owner);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    Item toItem(ItemDto item, User owner);
}
