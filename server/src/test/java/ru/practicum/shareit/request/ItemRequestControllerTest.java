package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.PracticumTestContext;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.util.Headers;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest extends PracticumTestContext {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testGet() throws Exception {
        long userId = 1L;
        long requestId = 1L;
        ItemRequestInfoDto request = new ItemRequestInfoDto(request1.getId(), request1.getDescription(),
                request1.getCreated(), null);

        when(itemRequestService.findById(requestId))
                .thenReturn(request);

        mvc.perform(get("/requests/" + requestId)
                        .header(Headers.USER_ID, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.created", is(request1.getCreated().format(pattern))));
        verify(itemRequestService, times(1)).findById(requestId);
    }

    @Test
    void testCreate() throws Exception {
        long userId = 1L;
        ItemRequestCreateDto newRequest = new ItemRequestCreateDto(request1.getDescription());

        when(itemRequestService.add(eq(userId), any()))
                .thenReturn(request1);

        mvc.perform(post("/requests")
                        .header(Headers.USER_ID, userId)
                        .content(mapper.writeValueAsString(newRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.created", is(request1.getCreated().format(pattern))));
        verify(itemRequestService, times(1)).add(eq(userId), any());
    }

    @Test
    void testFindMy() throws Exception {
        long userId = 1L;

        when(itemRequestService.findMy(userId)).thenReturn(Collections.emptyList());

        mvc.perform(get("/requests")
                        .header(Headers.USER_ID, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemRequestService, times(1)).findMy(userId);
    }

    @Test
    void testFindAll() throws Exception {
        long userId = 1L;

        when(itemRequestService.findAll(userId)).thenReturn(Collections.emptyList());

        mvc.perform(get("/requests/all")
                        .header(Headers.USER_ID, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemRequestService, times(1)).findAll(userId);
    }

}
