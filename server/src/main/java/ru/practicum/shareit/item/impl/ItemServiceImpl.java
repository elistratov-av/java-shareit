package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemInfoDto get(long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));

        List<Booking> bookings = bookingRepository.findByItemIdAndStartAfterOrderByStart(id, LocalDateTime.now());
        List<Comment> comments = commentRepository.findByItemId(id, Sort.by(Sort.Direction.DESC, "created"));

        return itemMapper.toItemInfoDto(item, bookings, comments);
    }

    @Override
    public List<ItemInfoDto> findByOwnerId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));

        List<Item> items = itemRepository.findByOwnerId(userId);
        List<Long> itemIds = items.stream().map(Item::getId).toList();
        List<Booking> bookings = bookingRepository.findByItemIdInAndStartAfterOrderByStart(itemIds, LocalDateTime.now());
        List<Comment> comments = commentRepository.findByItemIdIn(itemIds, Sort.by(Sort.Direction.DESC, "created"));

        return mergeWithItems(items, bookings, comments);
    }

    private List<ItemInfoDto> mergeWithItems(List<Item> items, List<Booking> bookings, List<Comment> comments) {
        Map<Long, List<Booking>> bookingsMapByItemId = new HashMap<>();
        for (Booking b : bookings) {
            bookingsMapByItemId.computeIfAbsent(b.getItem().getId(), k -> new ArrayList<>()).add(b);
        }
        Map<Long, List<Comment>> commentsMapByItemId = new HashMap<>();
        for (Comment c : comments) {
            commentsMapByItemId.computeIfAbsent(c.getItem().getId(), k -> new ArrayList<>()).add(c);
        }
        return items.stream().map(i -> itemMapper.toItemInfoDto(i,
                bookingsMapByItemId.get(i.getId()),
                commentsMapByItemId.get(i.getId())
        )).toList();
    }

    @Override
    public ItemDto add(ItemDto newItem, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));
        ItemRequest request = null;
        if (newItem.getRequestId() != null) {
            long requestId = newItem.getRequestId();
            request = itemRequestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден"));
        }
        Item item = itemRepository.save(itemMapper.toItem(newItem, owner, request));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto newItemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец с id = " + userId + " не найден"));

        Item newItem = itemMapper.toItem(newItemDto, owner);
        // проверяем необходимые условия
        Item oldItem = itemRepository.findById(newItem.getId())
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + newItem.getId() + " не найдена"));
        if (!newItem.getOwner().equals(oldItem.getOwner()))
            throw new ConditionsNotMetException("Редактировать разрешено только владельцу");

        // если вещь найдена и все условия соблюдены, обновляем ее содержимое
        if (StringUtils.isNoneBlank(newItem.getName())) oldItem.setName(newItem.getName());
        if (StringUtils.isNoneBlank(newItem.getDescription())) oldItem.setDescription(newItem.getDescription());
        if (newItem.getAvailable() != null) oldItem.setAvailable(newItem.getAvailable());

        Item item = itemRepository.save(oldItem);
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (StringUtils.isBlank(text))
            return new ArrayList<>();
        return itemMapper.toItemDtoList(itemRepository.searchByNameOrDescription(text));
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentCreateDto newComment) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
        LocalDateTime created = LocalDateTime.now();
        if (!bookingRepository.existsByBookerIdAndItemIdAndStartLessThanEqual(userId, itemId, created))
            throw new ConditionsNotMetException("Оставлять отзывы на вещь можно после того, как взяли её в аренду");

        Comment comment = commentRepository.save(commentMapper.map(newComment.getText(), item, author, created));
        return commentMapper.map(comment);
    }
}
