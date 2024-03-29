package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.LastBooking;
import ru.practicum.shareit.booking.dto.NextBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private ItemRequestService itemRequestService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentService commentService;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;
    @InjectMocks
    private ItemServiceImpl itemService;


    User owner = new User(1L, "userName", "email@mail.com");
    User wrongOwner = new User(99L, "user99Name", "email99@mail.com");
    User requestor = new User(2L, "requestorName", "requestorEmail@mail.com");
    ItemRequest request = new ItemRequest(1L, "requestDescription", requestor, null, null);

    ItemDto itemToSave = new ItemDto(1L, "itemName", "itemDescription", true, request);
    Item item = new Item(2L, "itemName", "itemDescription", true, owner, request);
    Item item2 = new Item(3L, "updateItemName", "updateItemDescription", true, owner, request);


    @Test
    void create_whenItemDtoIsValid_thenSaveItem() {
        Item item = ItemMapper.toItem(owner, itemToSave);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRequestService.findById(owner.getId(), itemToSave.getRequestId())).thenReturn(request);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.create(owner.getId(), itemToSave);

        verify(itemRepository, times(1)).save(any(Item.class));
        assertThat(actualItem.getId()).isEqualTo(itemToSave.getId());
        assertThat(actualItem.getName()).isEqualTo(itemToSave.getName());
        assertThat(actualItem.getDescription()).isEqualTo(itemToSave.getDescription());
        assertThat(actualItem.getRequestId()).isEqualTo(request.getId());
    }

    @Test
    void create_whenItemDtoIsValidAndItemGetRequestIdIsnull_thenSaveItem() {
        ItemDto itemToSave = new ItemDto(1L, "itemName", "itemDescription", true, null);

        Item item = ItemMapper.toItem(owner, itemToSave);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.create(owner.getId(), itemToSave);

        verify(itemRepository, times(1)).save(any(Item.class));
        assertThat(actualItem.getId()).isEqualTo(itemToSave.getId());
        assertThat(actualItem.getName()).isEqualTo(itemToSave.getName());
        assertThat(actualItem.getDescription()).isEqualTo(itemToSave.getDescription());
        assertThat(actualItem.getRequestId()).isEqualTo(null);
    }

    @Test
    void create_whenItemDtoIsValidAndWhenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(wrongOwner.getId())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemService.create(wrongOwner.getId(), itemToSave));

        verify(itemRepository, times(0)).save(any(Item.class));
        assertEquals(notFoundException.getMessage(), "User not found.");
    }


    @Test
    void update_whenItemDtoIsValid_thenSaveItem() {
        long itemId = 1L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
        ItemDto updateItem = new ItemDto(itemId, "updateItemName", "updateItemDescription", true, request);
        when(itemRepository.getReferenceById(updateItem.getId())).thenReturn(ItemMapper.toItem(owner, oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(owner, oldItem));

        itemService.update(itemId, updateItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        ItemDto savedItemDto = ItemMapper.toItemDto(itemArgumentCaptor.getValue());

        assertEquals(itemId, savedItemDto.getId());
        assertEquals("updateItemName", savedItemDto.getName());
        assertEquals("updateItemDescription", savedItemDto.getDescription());
    }

    @Test
    void update_whenItemDtoIsValidItemDtoGetNameIsBlank_thenSaveItem() {
        long itemId = 1L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
        ItemDto updateItem = new ItemDto(itemId, " ", "", true, request);
        when(itemRepository.getReferenceById(updateItem.getId())).thenReturn(ItemMapper.toItem(owner, oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(owner, oldItem));

        itemService.update(itemId, updateItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        ItemDto savedItemDto = ItemMapper.toItemDto(itemArgumentCaptor.getValue());

        assertEquals(itemId, savedItemDto.getId());
        assertEquals("itemName", savedItemDto.getName());
        assertEquals("itemDescription", savedItemDto.getDescription());
    }

    @Test
    void update_whenItemDtoIsValidAndAvailableNull_thenSaveItem() {
        long itemId = 1L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
        ItemDto updateItem = new ItemDto(itemId, "updateName", "updateDescription", null, request);
        when(itemRepository.getReferenceById(updateItem.getId())).thenReturn(ItemMapper.toItem(owner, oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(owner, oldItem));

        itemService.update(itemId, updateItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        ItemDto savedItemDto = ItemMapper.toItemDto(itemArgumentCaptor.getValue());

        assertEquals(itemId, savedItemDto.getId());
        assertEquals("updateName", savedItemDto.getName());
        assertEquals("updateDescription", savedItemDto.getDescription());
        assertTrue(savedItemDto.getAvailable());
    }

    @Test
    void update_whenItemDtoIsValidAndDescriptionIsBlank_thenSaveItem() {
        long itemId = 1L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
        ItemDto updateItem = new ItemDto(itemId, "updateName", "", true, request);
        when(itemRepository.getReferenceById(updateItem.getId())).thenReturn(ItemMapper.toItem(owner, oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(owner, oldItem));

        itemService.update(itemId, updateItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        ItemDto savedItemDto = ItemMapper.toItemDto(itemArgumentCaptor.getValue());

        assertEquals(itemId, savedItemDto.getId());
        assertEquals("updateName", savedItemDto.getName());
        assertEquals("itemDescription", savedItemDto.getDescription());
    }

    @Test
    void update_whenItemDtoIsValidAndItemDtoGetNameIsNull_thenSaveItem() {
        long itemId = 1L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
        ItemDto updateItem = new ItemDto(itemId, null, "", true, request);
        when(itemRepository.getReferenceById(updateItem.getId())).thenReturn(ItemMapper.toItem(owner, oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(owner, oldItem));

        itemService.update(itemId, updateItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        ItemDto savedItemDto = ItemMapper.toItemDto(itemArgumentCaptor.getValue());

        assertEquals(itemId, savedItemDto.getId());
        assertEquals("itemName", savedItemDto.getName());
        assertEquals("itemDescription", savedItemDto.getDescription());
    }

    @Test
    void update_whenItemDtoIsValidItemDtoGetDescriptionIsNull_thenSaveItem() {
        long itemId = 1L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
        ItemDto updateItem = new ItemDto(itemId, "", null, true, request);
        when(itemRepository.getReferenceById(updateItem.getId())).thenReturn(ItemMapper.toItem(owner, oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(owner, oldItem));

        itemService.update(itemId, updateItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        ItemDto savedItemDto = ItemMapper.toItemDto(itemArgumentCaptor.getValue());

        assertEquals(itemId, savedItemDto.getId());
        assertEquals("itemName", savedItemDto.getName());
        assertEquals("itemDescription", savedItemDto.getDescription());
    }

    @Test
    void update_whenItemDtoIsValidAndWhenUserIsNotOwner_thenNotFoundExceptionThrown() {
        long itemId = 1L;
        ItemDto oldItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
        ItemDto updateItem = new ItemDto(itemId, "updateItemName", "updateItemDescription", true, request);
        when(itemRepository.getReferenceById(updateItem.getId())).thenReturn(ItemMapper.toItem(owner, oldItem));
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> itemService.update(requestor.getId(), updateItem));

        verify(itemRepository, times(0)).save(any(Item.class));
        assertEquals(notFoundException.getMessage(), "The user is not the owner of the item.");
    }

    @Test
    void getItemById_whenItemFound_thenReturnItem() {
        long itemId = 1L;
        ItemDto exceptedItem = new ItemDto(itemId, "itemName", "itemDescription", true, request);
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
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> itemService.getItemById(owner.getId(), itemId));
        assertEquals(badRequestException.getMessage(), "Item id is null.");
    }

    @Test
    void getItemById_whenItemIdIsNotValid_thenNotFoundExceptionThrown() {
        Long itemId = 99L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemService.getItemById(owner.getId(), itemId));
        assertEquals(notFoundException.getMessage(), "Item not found.");
    }

    @Test
    void getAllItemsByUserId_whenItemsFoundAndFromNotNullAndSizeNotNull_thenReturnItemCollectionInList() {
        int from = 0;
        int size = 5;
        List<Item> exceptedItems = List.of(item, item2);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sort);
        Mockito.when(itemRepository.findByOwnerId(owner.getId(), page)).thenReturn(exceptedItems);

        List<Item> response = itemService.getAllItemsByUserId(owner.getId(), from, size).stream().map(i -> ItemMapper.toItem(owner, i)).collect(Collectors.toList());

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

        List<Item> response = itemService.searchItems(query, from, size).stream().map(i -> ItemMapper.toItem(owner, i)).collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(response.get(0).getId(), exceptedItems.get(0).getId());
        assertEquals(response.get(0).getName(), exceptedItems.get(0).getName());
        assertEquals(response.get(0).getDescription(), exceptedItems.get(0).getDescription());
        assertEquals(response.get(1).getId(), exceptedItems.get(1).getId());
        assertEquals(response.get(1).getName(), exceptedItems.get(1).getName());
        assertEquals(response.get(1).getDescription(), exceptedItems.get(1).getDescription());
    }

    @Test
    void searchItems_whenQueryIsNul_thenReturnItemCollectionEmptyList() {
        int from = 0;
        int size = 2;
        String query = "";

        List<Item> response = itemService.searchItems(query, from, size).stream()
                .map(i -> ItemMapper.toItem(owner, i)).collect(Collectors.toList());

        assertNotNull(response);
        assertEquals(response.size(), 0);
    }

    @Test
    void deleteById() {
        Item item = ItemMapper.toItem(owner, itemToSave);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRequestService.findById(owner.getId(), itemToSave.getRequestId())).thenReturn(request);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.create(owner.getId(), itemToSave);

        verify(itemRepository, times(1)).save(any(Item.class));
        assertThat(actualItem.getId()).isEqualTo(itemToSave.getId());
        assertThat(actualItem.getName()).isEqualTo(itemToSave.getName());
        assertThat(actualItem.getDescription()).isEqualTo(itemToSave.getDescription());
        assertThat(actualItem.getRequestId()).isEqualTo(request.getId());

        itemService.deleteById(owner.getId());


        when(itemRepository.findById(owner.getId())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemService.getItemById(owner.getId(), itemToSave.getId()));
        assertEquals(notFoundException.getMessage(), "Item not found.");
    }

    @Test
    void testGetBookings() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LocalDateTime.now().minusHours(2));
        booking1.setEnd(LocalDateTime.now().plusHours(1));
        booking1.setBooker(new User(1L, "userName", "userEmail@mail.com"));

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().plusHours(4));
        booking2.setEnd(LocalDateTime.now().plusHours(5));
        booking2.setBooker(new User(2L, "userName2", "userEmail2@mail.com"));

        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStart(LocalDateTime.now().minusHours(4));
        booking3.setEnd(LocalDateTime.now().minusHours(2));
        booking3.setBooker(new User(3L, "userName3", "userEmail3@mail.com"));

        Booking booking4 = new Booking();
        booking4.setId(3L);
        booking4.setStart(LocalDateTime.now().plusHours(8));
        booking4.setEnd(LocalDateTime.now().plusHours(9));
        booking4.setBooker(new User(4L, "userName4", "userEmail4@mail.com"));


        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        bookings.add(booking3);
        bookings.add(booking3);

        when(bookingRepository.findByItemIdAndStatus(eq(itemDto.getId()), eq(Booking.Status.APPROVED),
                any(Sort.class))).thenReturn(bookings);


        itemService.getBookings(itemDto);


        LastBooking lastBooking = itemDto.getLastBooking();
        Assertions.assertEquals(1L, lastBooking.getId());
        Assertions.assertNotNull(lastBooking.getBookerId());

        NextBooking nextBooking = itemDto.getNextBooking();
        Assertions.assertEquals(2L, nextBooking.getId());
        Assertions.assertNotNull(nextBooking.getBookerId());
    }
}