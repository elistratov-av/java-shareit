package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> get(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> findByOwnerId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> add(long userId, ItemCreateDto newItem) {
        return post("", userId, newItem);
    }

    public ResponseEntity<Object> update(long userId, ItemDto newItem) {
        return patch("/" + newItem.getId(), userId, newItem);
    }

    public ResponseEntity<Object> search(long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentCreateDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}
