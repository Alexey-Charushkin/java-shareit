package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item_request.dto.ItemRequestMapper;
import ru.practicum.shareit.item_request.model.ItemRequest;
import ru.practicum.shareit.item_request.service.ItemRequestService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private ItemRequestService itemRequestService;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @InjectMocks
    private ItemServiceImpl itemService;


    User owner = new User(0L, "userName", "email@mail.com");
    User wrongOwner = new User(99L, "user99Name", "email99@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(0L, "requestDescription", requestor);

    ItemDto itemToSave = new ItemDto(0L, "itemName", "itemDescription",
            true, request);
    Item item = new Item(0L, "itemName", "itemDescription",
            true, owner, request);
    Item item2 = new Item(1L, "updateItemName", "updateItemDescription",
            true, owner, request);


    @Test
    void create_whenItemDtoIsValid_thenSaveItem() {
        Item item = ItemMapper.toItem(owner, itemToSave);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRequestService.findById(owner.getId(), itemToSave.getRequestId()))
                .thenReturn(ItemRequestMapper.toItemRequestDto(request));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.create(owner.getId(), itemToSave);

        verify(itemRepository, times(1)).save(any(Item.class));
        assertThat(actualItem.getId()).isEqualTo(itemToSave.getId());
        assertThat(actualItem.getName()).isEqualTo(itemToSave.getName());
        assertThat(actualItem.getDescription()).isEqualTo(itemToSave.getDescription());
        assertThat(actualItem.getRequestId()).isEqualTo(request.getId());
    }

    @Test
    void create_whenItemDtoIsValidAndWhenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(wrongOwner.getId())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.create(wrongOwner.getId(), itemToSave));

        verify(itemRepository, times(0)).save(any(Item.class));
        assertEquals(notFoundException.getMessage(), "User not found.");
    }

    @Test
    void update_whenItemDtoIsValid_thenSaveItem() {
        long itemId = 0L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription",
                true, request);
        ItemDto updateItem = new ItemDto(itemId, "updateItemName", "updateItemDescription",
                true, request);
        when(itemRepository.getReferenceById(updateItem.getId()))
                .thenReturn(ItemMapper.toItem(owner, oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(owner, oldItem));

        itemService.update(itemId, updateItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        ItemDto savedItemDto = ItemMapper.toItemDto(itemArgumentCaptor.getValue());

        assertEquals(itemId, savedItemDto.getId());
        assertEquals("updateItemName", savedItemDto.getName());
        assertEquals("updateItemDescription", savedItemDto.getDescription());
    }

    @Test
    void getItemById_whenItemFound_thenReturnItem() {
        long itemId = 0L;
        ItemDto exceptedItem = new ItemDto(itemId, "itemName", "itemDescription",
                true, request);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(ItemMapper.toItem(owner, exceptedItem)));

        Optional<ItemDto> actualItem = Optional.ofNullable(itemService.getItemById(owner.getId(), itemId));

        verify(itemRepository, times(1)).findById(itemId);
        assertThat(exceptedItem.getId()).isEqualTo(actualItem.get().getId());
        assertThat(exceptedItem.getName()).isEqualTo(actualItem.get().getName());
        assertThat(exceptedItem.getDescription()).isEqualTo(actualItem.get().getDescription());
    }

    @Test
    void getItemById_whenItemIdIsNull_thenBadRequestExceptionThrown() {
        Long itemId = null;
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> itemService.getItemById(owner.getId(), itemId));
        assertEquals(badRequestException.getMessage(), "Item id is null.");
    }

    @Test
    void getItemById_whenItemIdIsNotValid_thenNotFoundExceptionThrown() {
        Long itemId = 99L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(owner.getId(), itemId));
        assertEquals(notFoundException.getMessage(), "Item not found.");
    }

    @Test
    void getAllItemsByUserId_whenItemsFoundAndFromIsNullAndSizeIsNull_thenReturnItemCollectionInList() {
        List<Item> exceptedItems = List.of(item, item2);
        Mockito.when(itemRepository.findByOwnerId(owner.getId(),
                Sort.by(Sort.Direction.ASC, "id"))).thenReturn(exceptedItems);

        List<Item> response = itemService.getAllItemsByUserId
                        (owner.getId(), null, null)
                .stream()
                .map(i -> ItemMapper.toItem(owner, i))
                .collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(response.get(0).getId(), exceptedItems.get(0).getId());
        assertEquals(response.get(0).getName(), exceptedItems.get(0).getName());
        assertEquals(response.get(0).getDescription(), exceptedItems.get(0).getDescription());
        assertEquals(response.get(1).getId(), exceptedItems.get(1).getId());
        assertEquals(response.get(1).getName(), exceptedItems.get(1).getName());
        assertEquals(response.get(1).getDescription(), exceptedItems.get(1).getDescription());
    }

    @Test
    void getAllItemsByUserId_whenItemsNotFound_thenReturnCollectionEmptyList() {
        List<ItemDto> response = itemService.getAllItemsByUserId
                (wrongOwner.getId(), null, null);

        assertEquals(response, Collections.emptyList());
    }

    @Test
    void getAllItemsByUserId_whenItemsFoundAndFromNotNullAndSizeNotNull_thenReturnItemCollectionInList() {
        int from = 0;
        int size = 2;
        List<Item> exceptedItems = List.of(item, item2);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        Mockito.when(itemRepository.findByOwnerId(owner.getId(), page
        )).thenReturn(exceptedItems);

        List<Item> response = itemService.getAllItemsByUserId
                        (owner.getId(), from, size)
                .stream()
                .map(i -> ItemMapper.toItem(owner, i))
                .collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(response.get(0).getId(), exceptedItems.get(0).getId());
        assertEquals(response.get(0).getName(), exceptedItems.get(0).getName());
        assertEquals(response.get(0).getDescription(), exceptedItems.get(0).getDescription());
        assertEquals(response.get(1).getId(), exceptedItems.get(1).getId());
        assertEquals(response.get(1).getName(), exceptedItems.get(1).getName());
        assertEquals(response.get(1).getDescription(), exceptedItems.get(1).getDescription());
    }

    @Test
    void searchItems_whenItemsFoundAndFromIsNullAndSizeIsNull_thenReturnItemCollectionInList() {
        List<Item> exceptedItems = List.of(item, item2);
        String query = "itemName";
        Mockito.when(itemRepository.search(query)).thenReturn(exceptedItems);

        List<Item> response = itemService.searchItems
                        (query, null, null)
                .stream()
                .map(i -> ItemMapper.toItem(owner, i))
                .collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(response.get(0).getId(), exceptedItems.get(0).getId());
        assertEquals(response.get(0).getName(), exceptedItems.get(0).getName());
        assertEquals(response.get(0).getDescription(), exceptedItems.get(0).getDescription());
        assertEquals(response.get(1).getId(), exceptedItems.get(1).getId());
        assertEquals(response.get(1).getName(), exceptedItems.get(1).getName());
        assertEquals(response.get(1).getDescription(), exceptedItems.get(1).getDescription());
    }

    @Test
    void searchItems_whenItemsFoundAndFromNotNullAndSizeNotNull_thenReturnItemCollectionInList() {
        int from = 0;
        int size = 2;
        List<Item> exceptedItems = List.of(item, item2);
        String query = "itemName";
        Pageable page = PageRequest.of(from, size);
        Mockito.when(itemRepository.searchToPage(query, page)).thenReturn(exceptedItems);

        List<Item> response = itemService.searchItems
                        (query, from, size)
                .stream()
                .map(i -> ItemMapper.toItem(owner, i))
                .collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(response.get(0).getId(), exceptedItems.get(0).getId());
        assertEquals(response.get(0).getName(), exceptedItems.get(0).getName());
        assertEquals(response.get(0).getDescription(), exceptedItems.get(0).getDescription());
        assertEquals(response.get(1).getId(), exceptedItems.get(1).getId());
        assertEquals(response.get(1).getName(), exceptedItems.get(1).getName());
        assertEquals(response.get(1).getDescription(), exceptedItems.get(1).getDescription());
    }

    @Test
    void searchItems_whenItemsNotFound_thenNotFoundExceptionThrown() {
        String query = "Not";
        Mockito.when(itemRepository.search(query)).thenThrow(EntityNotFoundException.class);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.searchItems(query, null, null));
        assertEquals(notFoundException.getMessage(), "Item not found.");
    }

    @Test
    void deleteById() {
        Item item = ItemMapper.toItem(owner, itemToSave);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRequestService.findById(owner.getId(), itemToSave.getRequestId()))
                .thenReturn(ItemRequestMapper.toItemRequestDto(request));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.create(owner.getId(), itemToSave);

        verify(itemRepository, times(1)).save(any(Item.class));
        assertThat(actualItem.getId()).isEqualTo(itemToSave.getId());
        assertThat(actualItem.getName()).isEqualTo(itemToSave.getName());
        assertThat(actualItem.getDescription()).isEqualTo(itemToSave.getDescription());
        assertThat(actualItem.getRequestId()).isEqualTo(request.getId());

        itemService.deleteById(owner.getId());


        when(itemRepository.findById(owner.getId())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(owner.getId(), itemToSave.getId()));
        assertEquals(notFoundException.getMessage(), "Item not found.");
    }
}