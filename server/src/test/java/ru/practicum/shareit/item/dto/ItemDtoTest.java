package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.PracticumTestContext;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest extends PracticumTestContext {
    private final JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws Exception {
        JsonContent<ItemDto> result = json.write(item1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(item1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item1.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item1.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isNull();
    }

}
