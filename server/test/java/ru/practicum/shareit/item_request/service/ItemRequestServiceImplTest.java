package java.ru.practicum.shareit.item_request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoToReturn;
import ru.practicum.shareit.request.dto.ItemRequestDtoToSave;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    User owner = new User(0L, "userName", "email@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(0L, "requestDescription", requestor, null, null);
    ItemRequest request2 = new ItemRequest(1L, "request2Description", requestor, null, null);
    ItemRequestDtoToReturn requestDto = ItemRequestMapper.toItemRequestDto(request);
    ItemRequestDtoToReturn requestDto2 = ItemRequestMapper.toItemRequestDto(request2);

    ItemRequestDtoToSave requestDtoToSave = new ItemRequestDtoToSave("new Request");

    ItemRequestDtoToSave requestDtoToSave2 = new ItemRequestDtoToSave("");

    List<ItemRequest> itemRequestList = List.of(request, request2);
    List<ItemRequestDtoToReturn> itemRequestDtoToReturnList = List.of(requestDto, requestDto2);

    @Test
    void create_whenItemRequestDtoIsValid_thenSaveItemRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(request);

        ItemRequestDtoToReturn actualItemRequest = itemRequestService.create(0L, requestDtoToSave);

        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));

        assertThat(actualItemRequest.getDescription()).isEqualTo(requestDtoToSave.getDescription());
    }

    @Test
    void create_whenItemRequestDtoNotValidUserNotFound_thenNotFondExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.create(0L, requestDtoToSave));
        assertEquals(notFoundException.getMessage(), "User not found");
    }

    @Test
    void create_whenItemRequestDtoNotValidDescriptionIsEmpty_thenBadRequestExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> itemRequestService.create(0L, requestDtoToSave2));
        assertEquals(badRequestException.getMessage(), "Description is empty");

    }

    @Test
    void findAllByUserId_whenUserIdIsCorrect_thenReturnList() {
        List<ItemRequestDtoToReturn> exceptedList = new ArrayList<>(itemRequestDtoToReturnList);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findAllByRequestorId(0L, Sort.by(Sort.Direction.DESC,
                "id"))).thenReturn(itemRequestList);

        List<ItemRequestDtoToReturn> actualList = itemRequestService.findAllByUserId(0L);

        verify(itemRequestRepository, times(1)).findAllByRequestorId(0L, Sort.by(Sort.Direction.DESC,
                "id"));
        assertEquals(exceptedList.get(0).getId(), actualList.get(0).getId());
        assertEquals(exceptedList.get(0).getDescription(), actualList.get(0).getDescription());
        assertEquals(exceptedList.get(1).getId(), actualList.get(1).getId());
        assertEquals(exceptedList.get(1).getDescription(), actualList.get(1).getDescription());
    }

    @Test
    void findAllByUserId_whenUserIdNotValidUserNotFound_thenNotFondExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.findAllByUserId(0L));
        assertEquals(notFoundException.getMessage(), "User not found");
    }

    @Test
    void findById_whenUserIdAndRequestIdIsCorrect_thenReturnItemRequestDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        ItemRequest itemRequest = itemRequestService.findById(0L, 0L);

        verify(itemRequestRepository, times(1)).findById(anyLong());
        assertEquals(itemRequest.getId(), request.getId());
        assertEquals(itemRequest.getDescription(), request.getDescription());
    }

    @Test
    void findById_whenUserIdNotCorrectAndRequestIdIsCorrect_thenNotFoundExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.findById(0L, 0L));
        assertEquals(notFoundException.getMessage(), "User not found");

    }

    @Test
    void findById_whenUserIdIsCorrectAndRequestIdNotCorrect_thenNotFoundExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.findById(0L, 0L));
        assertEquals(notFoundException.getMessage(), "ItemRequest not found");
    }

    @Test
    void findAllByUserIdToPageable_whenUserIdIsCorrect_thenReturnList() {
        int from = 0;
        int size = 3;
        List<ItemRequestDtoToReturn> exceptedList = new ArrayList<>(itemRequestDtoToReturnList);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findAllByRequestorIdNot(anyLong(), any(Pageable.class)))
                .thenReturn(itemRequestList);

        List<ItemRequestDtoToReturn> actualList = itemRequestService.findAllByUserIdToPageable(
                0L, from, size);

        verify(itemRequestRepository, times(1))
                .findAllByRequestorIdNot(anyLong(), any(Pageable.class));
        assertEquals(exceptedList.get(0).getId(), actualList.get(0).getId());
        assertEquals(exceptedList.get(0).getDescription(), actualList.get(0).getDescription());
        assertEquals(exceptedList.get(1).getId(), actualList.get(1).getId());
        assertEquals(exceptedList.get(1).getDescription(), actualList.get(1).getDescription());
    }

    @Test
    void findAllByUserIdToPageable_whenUserIdNotCorrectAndRequestIdIsCorrect_thenNotFoundExceptionThrown() {
        int from = 0;
        int size = 3;
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemRequestService.findAllByUserIdToPageable(
                        anyLong(), from, size));
        assertEquals(notFoundException.getMessage(), "User not found");

    }

    @Test
    void findAllByUserIdToPageable_whenUserIdIsCorrectAndRequestIdIsCorrectAndFromIsNullAndSizeIsNull_thenReturnEmptyList() {
        List<ItemRequestDtoToReturn> exceptedList = Collections.emptyList();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));

        List<ItemRequestDtoToReturn> actualList = itemRequestService.findAllByUserIdToPageable(
                0L, null, null);

        assertEquals(actualList, exceptedList);
    }
}