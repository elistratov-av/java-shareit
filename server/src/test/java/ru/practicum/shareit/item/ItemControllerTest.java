package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.PracticumTestContext;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.util.Headers;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest extends PracticumTestContext {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void testGet() throws Exception {
        long userId = 1L;
        long itemId = 1L;
        ItemInfoDto item = new ItemInfoDto(item1.getId(), item1.getName(), item1.getDescription(),
                item1.getAvailable(), null, null, null, null);

        when(itemService.get(itemId))
                .thenReturn(item);

        mvc.perform(get("/items/" + itemId)
                        .header(Headers.USER_ID, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())));
        verify(itemService, times(1)).get(itemId);
    }

    @Test
    void testFindByOwner() throws Exception {
        long userId = 1L;

        when(itemService.findByOwnerId(userId)).thenReturn(Collections.emptyList());

        mvc.perform(get("/items")
                        .header(Headers.USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).findByOwnerId(userId);
    }

    @Test
    void testCreate() throws Exception {
        long userId = 1L;

        when(itemService.add(any(), eq(userId)))
                .thenReturn(item1);

        mvc.perform(post("/items")
                        .header(Headers.USER_ID, userId)
                        .content(mapper.writeValueAsString(item1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())));
        verify(itemService, times(1)).add(any(), eq(userId));
    }

    @Test
    void testUpdate() throws Exception {
        long userId = 1L;
        long itemId = 1L;

        when(itemService.update(any(), eq(userId)))
                .thenReturn(item1);

        mvc.perform(patch("/items/" + itemId)
                        .header(Headers.USER_ID, userId)
                        .content(mapper.writeValueAsString(item1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())));
        verify(itemService, times(1)).update(any(), eq(userId));
    }

    @Test
    void testUpdateWithException() throws Exception {
        long userId = 10L;
        long itemId = 1L;

        when(itemService.update(any(), eq(userId)))
                .thenThrow(ConditionsNotMetException.class);

        mvc.perform(patch("/items/" + itemId)
                        .header(Headers.USER_ID, userId)
                        .content(mapper.writeValueAsString(item1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void testSearch() throws Exception {
        long userId = 1L;
        String text = "UNKNOWN_TEXT";

        when(itemService.search(text)).thenReturn(Collections.emptyList());

        mvc.perform(get("/items/search")
                        .header(Headers.USER_ID, userId)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).search(text);
    }

    @Test
    void testAddComment() throws Exception {
        long userId = 1L;
        long itemId = 1L;
        CommentCreateDto newComment = new CommentCreateDto(comment1.getText());

        when(itemService.addComment(eq(userId), eq(itemId), any()))
                .thenReturn(comment1);

        mvc.perform(post("/items/" + itemId + "/comment")
                        .header(Headers.USER_ID, userId)
                        .content(mapper.writeValueAsString(newComment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment1.getText())))
                .andExpect(jsonPath("$.authorName", is(comment1.getAuthorName())));
        verify(itemService, times(1)).addComment(eq(userId), eq(itemId), any());
    }

}
