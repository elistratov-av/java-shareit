package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.PracticumTestContext;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemServiceTest extends PracticumTestContext {
    private final EntityManager em;
    private final ItemService itemService;

    @Test
    void testGet() {
        long itemId = 1L;

        ItemInfoDto item = itemService.get(itemId);

        assertThat(item1.getId(), equalTo(item.getId()));
        assertThat(item1.getName(), equalTo(item.getName()));
        assertThat(item1.getDescription(), equalTo(item.getDescription()));
        assertThat(item1.getAvailable(), equalTo(item.getAvailable()));
        assertThat(item1.getRequestId(), equalTo(item.getRequestId()));
    }

    @Test
    void testGetNotFound() {
        long itemId = 4L;

        assertThrows(
                NotFoundException.class,
                () -> itemService.get(itemId)
        );
    }

    @Test
    void testFindByOwnerId() {
        long userId = 1L;

        List<ItemInfoDto> items = itemService.findByOwnerId(userId);

        List<ItemDto> expectedItems = List.of(item1, item2, item3);
        assertThat(items, hasSize(expectedItems.size()));
        for (ItemDto expItem : expectedItems) {
            assertThat(items, hasItem(allOf(
                    hasProperty("id", equalTo(expItem.getId())),
                    hasProperty("name", equalTo(expItem.getName())),
                    hasProperty("description", equalTo(expItem.getDescription())),
                    hasProperty("available", equalTo(expItem.getAvailable())),
                    hasProperty("requestId", equalTo(expItem.getRequestId()))
            )));
        }
    }

    @Test
    void testFindByOwnerIdNotFound() {
        long userId = 4L;

        assertThrows(
                NotFoundException.class,
                () -> itemService.findByOwnerId(userId)
        );
    }

    @Test
    void testAdd() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(null, "new name", "new desc", true, null);

        ItemDto itemDto = itemService.add(newItem, userId);
        assertThat(itemDto.getId(), notNullValue());
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item actualItem = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(actualItem.getName(), equalTo(itemDto.getName()));
        assertThat(actualItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(actualItem.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(actualItem.getRequest(), nullValue());
    }

    @Test
    void testAddRequest() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(null, "new name", "new desc", true, 2L);

        ItemDto itemDto = itemService.add(newItem, userId);
        assertThat(itemDto.getId(), notNullValue());
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item actualItem = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(actualItem.getName(), equalTo(itemDto.getName()));
        assertThat(actualItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(actualItem.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(actualItem.getRequest(), notNullValue());
    }

    @Test
    void testAddNotFoundOwner() {
        long userId = 4L;
        ItemDto newItem = new ItemDto(null, "new name", "new desc", true, null);

        assertThrows(
                NotFoundException.class,
                () -> itemService.add(newItem, userId)
        );
    }

    @Test
    void testAddNotFoundRequest() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(null, "new name", "new desc", true, 4L);

        assertThrows(
                NotFoundException.class,
                () -> itemService.add(newItem, userId)
        );
    }

    @Test
    void testUpdate() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(1L, "new name", "new desc", true, null);

        ItemDto itemDto = itemService.update(newItem, userId);

        assertThat(newItem, equalTo(itemDto));
    }

    @Test
    void testUpdateBlankName() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(1L, "", "new desc", true, null);

        ItemDto itemDto = itemService.update(newItem, userId);
        assertThat(itemDto.getId(), notNullValue());
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item actualItem = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(actualItem.getName(), equalTo(itemDto.getName()));
        assertThat(actualItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(actualItem.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(actualItem.getRequest(), nullValue());
    }

    @Test
    void testUpdateBlankDescription() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(1L, "new name", "", true, null);

        ItemDto itemDto = itemService.update(newItem, userId);
        assertThat(itemDto.getId(), notNullValue());
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item actualItem = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(actualItem.getName(), equalTo(itemDto.getName()));
        assertThat(actualItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(actualItem.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(actualItem.getRequest(), nullValue());
    }

    @Test
    void testUpdateNullAvailable() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(1L, "new name", "new desc", null, null);

        ItemDto itemDto = itemService.update(newItem, userId);
        assertThat(itemDto.getId(), notNullValue());
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item actualItem = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(actualItem.getName(), equalTo(itemDto.getName()));
        assertThat(actualItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(actualItem.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(actualItem.getRequest(), nullValue());
    }

    @Test
    void testUpdateNotFoundOwner() {
        long userId = 4L;
        ItemDto newItem = new ItemDto(1L, "new name", "new desc", true, null);

        assertThrows(
                NotFoundException.class,
                () -> itemService.update(newItem, userId)
        );
    }

    @Test
    void testUpdateNotFoundItem() {
        long userId = 1L;
        ItemDto newItem = new ItemDto(4L, "new name", "new desc", true, null);

        assertThrows(
                NotFoundException.class,
                () -> itemService.update(newItem, userId)
        );
    }

    @Test
    void testUpdateNotOwner() {
        long userId = 2L;
        ItemDto newItem = new ItemDto(1L, "new name", "new desc", true, null);

        assertThrows(
                ConditionsNotMetException.class,
                () -> itemService.update(newItem, userId)
        );
    }

    @Test
    void testSearch() {
        String searchText = "name1";

        List<ItemDto> items = itemService.search(searchText);

        assertEquals(List.of(item1), items);
    }

    @Test
    void testSearchBlank() {
        String searchText = "";

        List<ItemDto> items = itemService.search(searchText);

        assertEquals(Collections.emptyList(), items);
    }

    @Test
    void testAddComment() {
        long userId = 1L;
        long itemId = 1L;
        CommentCreateDto newComment = new CommentCreateDto("new comment");

        CommentDto commentDto = itemService.addComment(userId, itemId, newComment);
        assertThat(commentDto.getId(), notNullValue());
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.id = :id", Comment.class);
        Comment actualComment = query.setParameter("id", commentDto.getId())
                .getSingleResult();

        assertThat(actualComment.getText(), equalTo(commentDto.getText()));
        assertThat(actualComment.getAuthor().getName(), equalTo(commentDto.getAuthorName()));
    }

    @Test
    void testAddCommentNotBooker() {
        long userId = 3L;
        long itemId = 1L;
        CommentCreateDto newComment = new CommentCreateDto("new comment");

        assertThrows(
                ConditionsNotMetException.class,
                () -> itemService.addComment(userId, itemId, newComment)
        );
    }

    @Test
    void testAddCommentNotFoundUser() {
        long userId = 4L;
        long itemId = 1L;
        CommentCreateDto newComment = new CommentCreateDto("new comment");

        assertThrows(
                NotFoundException.class,
                () -> itemService.addComment(userId, itemId, newComment)
        );
    }

    @Test
    void testAddCommentNotFoundItem() {
        long userId = 2L;
        long itemId = 4L;
        CommentCreateDto newComment = new CommentCreateDto("new comment");

        assertThrows(
                NotFoundException.class,
                () -> itemService.addComment(userId, itemId, newComment)
        );
    }

}
