package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto add(long requestorId, ItemRequestCreateDto newRequest) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + requestorId + " не найден"));

        ItemRequest itemRequest = itemRequestRepository.save(itemRequestMapper.map(newRequest, requestor, LocalDateTime.now()));
        return itemRequestMapper.map(itemRequest);
    }

    @Override
    public List<ItemRequestInfoDto> findMy(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(userId);
        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.findByRequestIdIn(itemRequestIds);

        return mergeWithItemRequests(itemRequests, items);
    }

    private List<ItemRequestInfoDto> mergeWithItemRequests(List<ItemRequest> itemRequests, List<Item> items) {
        Map<Long, List<Item>> itemsMapByItemRequestId = new HashMap<>();
        for (Item it : items) {
            itemsMapByItemRequestId.computeIfAbsent(it.getRequest().getId(), k -> new ArrayList<>()).add(it);
        }
        return itemRequests.stream().map(i -> itemRequestMapper.toItemRequestInfoDto(i,
                itemsMapByItemRequestId.get(i.getId())
        )).toList();
    }

    public ItemRequestInfoDto findById(long id) {
        ItemRequest request = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + id + " не найден"));
        List<Item> items = itemRepository.findByRequestId(id);

        return itemRequestMapper.toItemRequestInfoDto(request, items);
    }

    public List<ItemRequestDto> findAll(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));

        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdNot(userId, Sort.by(Sort.Direction.DESC, "created"));
        return itemRequestMapper.map(requests);
    }
}
