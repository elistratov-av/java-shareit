package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.PracticumTestContext;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest extends PracticumTestContext {
    private final JacksonTester<BookingDto> json;

    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testBookingDto() throws Exception {
        JsonContent<BookingDto> result = json.write(booking1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(booking1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking1.getStart().format(pattern));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking1.getEnd().format(pattern));
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(booking1.getItem()
                .getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(booking1.getBooker()
                .getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(booking1.getStatus().toString());
    }
}
