package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemMapperTest {
    @Autowired
    private JacksonTester<ItemDto> jsonItemDto;
    @Autowired
    private JacksonTester<Item> jsonItem;
    User owner = new User(0L, "userName", "email@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor);

    @SneakyThrows
    @Test
    void toItemDto() {
        ItemDto itemDto = new ItemDto(
                1L,
                "itemDtoName",
                "itemDtoDescription",
                true,
                request);

        JsonContent<ItemDto> result = jsonItemDto.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemDtoName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemDtoDescription");
        assertThat(result).toString().equals(itemDto.toString());
    }

    @SneakyThrows
    @Test
    void toItem() {
        Item item = new Item(
                1L,
                "itemName",
                "itemDescription",
                true,
                owner,
                request);

        JsonContent<Item> result = jsonItem.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemDescription");
        assertThat(result).toString().equals(item.toString());
    }
}