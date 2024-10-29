package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto add(long requestorId, ItemRequestCreateDto newRequest);

    List<ItemRequestInfoDto> findMy(long userId);

    ItemRequestInfoDto findById(long id);

    List<ItemRequestDto> findAll(long userId);
}
