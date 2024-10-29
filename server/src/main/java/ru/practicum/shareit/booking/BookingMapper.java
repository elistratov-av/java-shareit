package ru.practicum.shareit.booking;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(uses = ItemMapper.class)
@AnnotateWith(value = Component.class)
public interface BookingMapper {
    BookingDto map(Booking booking);

    List<BookingDto> map(Iterable<Booking> booking);

    @Mapping(target = "id", ignore = true)
    Booking map(BookingCreateDto booking, Item item, User booker, BookingStatus status);
}
