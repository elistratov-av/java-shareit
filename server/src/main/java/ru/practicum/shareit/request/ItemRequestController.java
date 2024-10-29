package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.util.Headers;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    // POST /requests
    @PostMapping
    public ItemRequestDto create(@RequestHeader(Headers.USER_ID) long userId, @RequestBody ItemRequestCreateDto newRequest) {
        log.info("==> create item request: {}, requestorId = {}", newRequest, userId);
        ItemRequestDto request = itemRequestService.add(userId, newRequest);
        log.info("<== create item request: {}, requestorId = {}", request, userId);
        return request;
    }

    // GET /requests
    @GetMapping
    public List<ItemRequestInfoDto> findMy(@RequestHeader(Headers.USER_ID) long userId) {
        log.info("==> findMy requestorId = {}", userId);
        List<ItemRequestInfoDto> requests = itemRequestService.findMy(userId);
        log.info("<== findMy {} requestorId = {}", requests.size(), userId);
        return requests;
    }

    // GET /requests/all
    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader(Headers.USER_ID) long userId) {
        log.info("==> findAll requestorId = {}", userId);
        List<ItemRequestDto> requests = itemRequestService.findAll(userId);
        log.info("<== findAll {} requestorId = {}", requests.size(), userId);
        return requests;
    }

    // GET /requests/{requestId}
    @GetMapping("/{requestId}")
    public ItemRequestInfoDto get(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long requestId) {
        log.info("==> findById requestorId = {}, requestId = {}", userId, requestId);
        ItemRequestInfoDto request = itemRequestService.findById(requestId);
        log.info("<== findById {} requestorId = {}", request, userId);
        return request;
    }
}
