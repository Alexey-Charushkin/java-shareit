package ru.practicum.shareit.item_request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item_request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toItemRequestToSave(ItemRequestDtoToSave itemRequestDtoToSave) {
        return new ItemRequest(
                itemRequestDtoToSave.getDescription()
        );
    }

//    public static ItemRequest toItemRequest(ItemRequestDtoToReturn itemRequestDtoToReturn) {
//        return new ItemRequest(
//                itemRequestDtoToReturn.getId(),
//                itemRequestDtoToReturn.getDescription(),
//                itemRequestDtoToReturn.getCreated(),
//                itemRequestDtoToReturn.getItems()
//        );
//    }

    public static ItemRequestDtoToReturn toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDtoToReturn(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemRequest.getItems()
        );
    }
}
