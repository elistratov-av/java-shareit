package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.PracticumTestContext;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemRequestServiceTest extends PracticumTestContext {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;

    @Test
    void testAdd() {
        long userId = 1L;
        ItemRequestCreateDto newRequest = new ItemRequestCreateDto("new description");

        ItemRequestDto requestDto = itemRequestService.add(userId, newRequest);
        assertThat(requestDto.getId(), notNullValue());
        TypedQuery<ItemRequest> query = em.createQuery("SELECT r FROM ItemRequest r WHERE r.id = :id", ItemRequest.class);
        ItemRequest actualRequest = query.setParameter("id", requestDto.getId())
                .getSingleResult();

        assertThat(actualRequest.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(actualRequest.getRequestor().getId(), equalTo(userId));
    }

    @Test
    void testFindByBooker() {
        long userId = 1L;

        List<ItemRequestInfoDto> requests = itemRequestService.findMy(userId);

        List<ItemRequestDto> expectedRequests = List.of(request1);
        assertThat(requests, hasSize(expectedRequests.size()));
        for (ItemRequestDto expRequest : expectedRequests) {
            assertThat(requests, hasItem(allOf(
                    hasProperty("id", equalTo(expRequest.getId())),
                    hasProperty("description", equalTo(expRequest.getDescription()))
            )));
        }
    }

    @Test
    void testFindById() {
        long requestId = 1L;

        ItemRequestInfoDto request = itemRequestService.findById(requestId);

        assertThat(request, notNullValue());
        assertThat(item1.getId(), equalTo(request.getId()));
        assertThat(item1.getDescription(), equalTo(request.getDescription()));
    }

    @Test
    void testFindAll() {
        long userId = 2L;

        List<ItemRequestDto> requests = itemRequestService.findAll(userId);

        List<ItemRequestDto> expectedRequests = List.of(request1);
        assertThat(requests, notNullValue());
        assertThat(expectedRequests, equalTo(requests));
    }

}
