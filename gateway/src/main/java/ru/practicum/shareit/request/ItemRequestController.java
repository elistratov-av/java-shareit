package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.util.Headers;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    // POST /requests
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Headers.USER_ID) long userId, @RequestBody @Valid ItemRequestCreateDto newItemRequest) {
        log.info("==> create item request: {}, requestorId = {}", newItemRequest, userId);
        ResponseEntity<Object> request = itemRequestClient.add(userId, newItemRequest);
        return request;
    }

    // GET /requests
    @GetMapping
    public ResponseEntity<Object> findMy(@RequestHeader(Headers.USER_ID) long userId) {
        log.info("==> findMy requestorId = {}", userId);
        ResponseEntity<Object> requests = itemRequestClient.findMy(userId);
        return requests;
    }

    // GET /requests/all
    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader(Headers.USER_ID) long userId) {
        log.info("==> findAll requestorId = {}", userId);
        ResponseEntity<Object> requests = itemRequestClient.findAll(userId);
        return requests;
    }

    // GET /requests/{requestId}
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> get(@RequestHeader(Headers.USER_ID) long userId, @PathVariable long requestId) {
        log.info("==> findById requestorId = {}, requestId = {}", userId, requestId);
        ResponseEntity<Object> request = itemRequestClient.findById(userId, requestId);
        return request;
    }
}
