package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest extends PracticumTestContext {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testCreate() throws Exception {
        long userId = 1L;

        when(bookingService.add(any(), eq(userId)))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header(Headers.USER_ID, userId)
                        .content(mapper.writeValueAsString(booking1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking1.getStart().format(pattern))))
                .andExpect(jsonPath("$.end", is(booking1.getEnd().format(pattern))))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
        verify(bookingService, times(1)).add(any(), eq(userId));
    }

    @Test
    void testCreateWithException() throws Exception {
        long userId = 10L;

        when(bookingService.add(any(), eq(userId)))
                .thenThrow(NotFoundException.class);

        mvc.perform(post("/bookings")
                        .header(Headers.USER_ID, userId)
                        .content(mapper.writeValueAsString(booking1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void testApprove() throws Exception {
        long userId = 1L;
        long bookingId = 2L;
        String approved = "true";

        when(bookingService.approve(any()))
                .thenReturn(booking2);

        mvc.perform(patch("/bookings/" + bookingId)
                        .header(Headers.USER_ID, userId)
                        .param("approved", approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking2.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking2.getStatus().toString())));
        verify(bookingService, times(1)).approve(any());
    }

    @Test
    void testApproveWithException() throws Exception {
        long userId = 2L;
        long bookingId = 1L;
        String approved = "true";

        when(bookingService.approve(any()))
                .thenThrow(ForbiddenException.class);

        mvc.perform(patch("/bookings/" + bookingId)
                        .header(Headers.USER_ID, userId)
                        .param("approved", approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void testGet() throws Exception {
        long userId = 1L;
        long bookingId = 1L;

        when(bookingService.get(bookingId, userId))
                .thenReturn(booking1);

        mvc.perform(get("/bookings/" + bookingId)
                        .header(Headers.USER_ID, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
        verify(bookingService, times(1)).get(bookingId, userId);
    }

    @Test
    void testFindByBooker() throws Exception {
        String state = "PAST";
        long userId = 1L;

        when(bookingService.findByBooker(userId, BookingState.PAST)).thenReturn(Collections.emptyList());

        mvc.perform(get("/bookings")
                        .header(Headers.USER_ID, userId)
                        .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).findByBooker(userId, BookingState.PAST);
    }

    @Test
    void testFindByOwner() throws Exception {
        String state = "PAST";
        long userId = 1L;

        when(bookingService.findByBooker(userId, BookingState.PAST)).thenReturn(Collections.emptyList());

        mvc.perform(get("/bookings/owner")
                        .header(Headers.USER_ID, userId)
                        .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).findByOwner(userId, BookingState.PAST);
    }

    @Test
    void testFindByOwnerWithException() throws Exception {
        String state = "UNKNOWN_VALUE";
        long userId = 1L;

        //when(bookingService.findByBooker(userId, BookingState.PAST)).thenReturn(Collections.emptyList());

        mvc.perform(get("/bookings/owner")
                        .header(Headers.USER_ID, userId)
                        .param("state", state))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
