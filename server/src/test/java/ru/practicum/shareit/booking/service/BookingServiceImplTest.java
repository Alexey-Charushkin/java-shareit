package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookItemResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;
    User owner = new User(0L, "userName", "email@mail.com");
    User wrongOwner = new User(99L, "user99Name", "email99@mail.com");
    User requestor = new User(1L, "requestorName", "requestorEmail@mail.com");
    User booker = new User(2L, "bookerName", "bookerEmil@mail.com");
    ItemRequest request = new ItemRequest(0L, "requestDescription", requestor, null, null);
    Item item = new Item(0L, "itemName", "itemDescription",
            true, owner, request);
    Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
            item, booker, "WAITING");
    Booking bookingToSave2 = new Booking(2L, LocalDateTime.now().plusMinutes(6), LocalDateTime.now().plusMinutes(10),
            item, booker, "WAITING");
    BookItemResponseDto bookingToSaveDto = BookingMapper.toBookingDto(bookingToSave);
    List<Booking> bookingList = new ArrayList<>(List.of(bookingToSave, bookingToSave2));

    @Test
    void create_whenBookingDtoIsValid_thenSaveBooking() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(bookingToSaveDto.getItemId()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingToSave);

        BookItemResponseDto actualBooking = bookingService.create(booker.getId(), bookingToSaveDto);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertThat(actualBooking.getId()).isEqualTo(bookingToSave.getId());
        assertThat(actualBooking.getStart()).isEqualTo(bookingToSave.getStart());
        assertThat(actualBooking.getEnd()).isEqualTo(bookingToSave.getEnd());
        assertThat(actualBooking.getItem()).isEqualTo(bookingToSave.getItem());
        assertThat(actualBooking.getBooker()).isEqualTo(bookingToSave.getBooker());
        assertThat(actualBooking.getStatus().toString()).isEqualTo(bookingToSave.getStatus().toString());
    }

    @Test
    void create_whenBookingDtoIsValidAndStartIsNull_thenBadRequestExceptionThrown() {
        Booking bookingToSave = new Booking(0L, null, LocalDateTime.now().plusMinutes(1),
                item, booker, "WAITING");
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> bookingService.create(booker.getId(), BookingMapper.toBookingDto(bookingToSave)));
        assertEquals(badRequestException.getMessage(), "Date time not correct.");
    }

    @Test
    void create_whenBookingDtoIsValidAndEndIsNull_thenBadRequestExceptionThrown() {
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), null,
                item, booker, "WAITING");
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> bookingService.create(booker.getId(), BookingMapper.toBookingDto(bookingToSave)));
        assertEquals(badRequestException.getMessage(), "Date time not correct.");
    }

    @Test
    void create_whenBookingDtoIsValidAndStartIsAfterEnd_thenBadRequestExceptionThrown() {
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusMinutes(1),
                item, booker, "WAITING");
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> bookingService.create(booker.getId(), BookingMapper.toBookingDto(bookingToSave)));
        assertEquals(badRequestException.getMessage(), "Date time not correct.");
    }

    @Test
    void create_whenBookingDtoIsValidAndStartIsBeforeEnd_thenBadRequestExceptionThrown() {
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusMinutes(1),
                item, booker, "WAITING");
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> bookingService.create(booker.getId(), BookingMapper.toBookingDto(bookingToSave)));
        assertEquals(badRequestException.getMessage(), "Date time not correct.");
    }

    @Test
    void create_whenBookingDtoIsValidAndBookerEqualsOwner_thenNotFundExceptionThrown() {
        Item item = new Item(0L, "itemName", "itemDescription",
                true, requestor, request);
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
                item, requestor, "WAITING");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.create(requestor.getId(), BookingMapper.toBookingDto(bookingToSave)));
        assertEquals(notFoundException.getMessage(), "Owner is not be booker.");
    }

    @Test
    void create_whenBookingDtoIsValidAndAvailableNotCorrect_thenNotBadRequestExceptionThrown() {
        Item item = new Item(0L, "itemName", "itemDescription",
                false, owner, request);
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
                item, requestor, "WAITING");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> bookingService.create(requestor.getId(), BookingMapper.toBookingDto(bookingToSave)));
        assertEquals(badRequestException.getMessage(), "Available false.");
    }

    @Test
    void findByBookingId_whenBookingIsFoundAndUserIsOwner_thenReturnBooking() {
        when(bookingRepository.findById(bookingToSave.getId()))
                .thenReturn(Optional.of(bookingToSave));

        Optional<BookItemResponseDto> actualBooking = Optional.of(bookingService.findByBookingId(bookingToSave.getBooker().getId(),
                bookingToSave.getId()));

        verify(bookingRepository, times(1)).findById(bookingToSave.getId());

        assertThat(actualBooking.get().getId()).isEqualTo(bookingToSave.getId());
        assertThat(actualBooking.get().getStart()).isEqualTo(bookingToSave.getStart());
        assertThat(actualBooking.get().getEnd()).isEqualTo(bookingToSave.getEnd());
        assertThat(actualBooking.get().getItem()).isEqualTo(bookingToSave.getItem());
        assertThat(actualBooking.get().getBooker()).isEqualTo(bookingToSave.getBooker());
        assertThat(actualBooking.get().getStatus().toString()).isEqualTo(bookingToSave.getStatus().toString());
    }

    @Test
    void findByBookingId_whenBookingIsFoundAndUserIsNotBookerOrIsNotOwner_thenNotfoundExceptionThrown() {
        Item item = new Item(0L, "itemName", "itemDescription",
                false, owner, request);
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
                item, requestor, "WAITING");

        when(bookingRepository.findById(bookingToSave.getId()))
                .thenReturn(Optional.of(bookingToSave));

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.findByBookingId(wrongOwner.getId(), bookingToSave.getId()));
        assertEquals(notFoundException.getMessage(), "This User is not Owner or not Booker of this Item.");
    }

    @Test
    void findByBookingId_whenBookingNotFound_thenNotFoundExceptionThrown() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.findByBookingId(owner.getId(), 99L));
        assertEquals(notFoundException.getMessage(), "Booking not found.");
    }

    @Test
    void approveBooking_whenBookingIsFoundAndUserIsOwnerToItemAndItemStatusIsWaitingAndApprovedIsTrue_ThenSaveBooking() {
        BookItemResponseDto bookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingRepository.findById(bookItemResponseDto.getId()))
                .thenReturn(Optional.of(bookingToSave));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingToSave);

        assertEquals(bookItemResponseDto.getStatus().toString(), Booking.Status.WAITING.toString());

        bookingService.approveBooking(bookItemResponseDto.getItem().getOwner().getId(),
                bookItemResponseDto.getId(), true);

        verify(bookingRepository).save(bookingArgumentCaptor.capture());

        BookItemResponseDto savedBookItemResponseDto = BookingMapper.toBookingDto(bookingArgumentCaptor.getValue());

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(savedBookItemResponseDto.getStatus().toString(), Booking.Status.APPROVED.toString());
    }

    @Test
    void approveBooking_whenBookingIsFoundAndUserIsOwnerToItemAndItemStatusIsWaitingAndApprovedIsFalse_ThenSaveBooking() {
        BookItemResponseDto bookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingRepository.findById(bookItemResponseDto.getId()))
                .thenReturn(Optional.of(bookingToSave));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingToSave);

        assertEquals(bookItemResponseDto.getStatus().toString(), Booking.Status.WAITING.toString());

        bookingService.approveBooking(bookItemResponseDto.getItem().getOwner().getId(),
                bookItemResponseDto.getId(), false);

        verify(bookingRepository).save(bookingArgumentCaptor.capture());

        BookItemResponseDto savedBookItemResponseDto = BookingMapper.toBookingDto(bookingArgumentCaptor.getValue());

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(savedBookItemResponseDto.getStatus().toString(), Booking.Status.REJECTED.toString());
    }

    @Test
    void approveBooking_whenBookingNotFoundAndUserIsOwnerToItemAndItemStatusIsWaitingAndApprovedIsFalse_ThenNotFoundExceptionThrown() {
        BookItemResponseDto bookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingRepository.findById(bookItemResponseDto.getId()))
                .thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(bookItemResponseDto.getItem().getOwner().getId(),
                        bookItemResponseDto.getId(), false));
        assertEquals(notFoundException.getMessage(), "Booking not found.");
    }

    @Test
    void approveBooking_whenBookingIsFoundAndUserIsOwnerToItemAndItemStatusIsApprovedAndApprovedIsFalse_ThenBadRequestExceptionThrown() {
        BookItemResponseDto bookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);
        bookingToSave.setStatus(Booking.Status.APPROVED);
        bookItemResponseDto.setStatus(BookItemResponseDto.StatusDto.APPROVED);
        when(bookingRepository.findById(bookItemResponseDto.getId()))
                .thenReturn(Optional.of(bookingToSave));

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> bookingService.approveBooking(bookItemResponseDto.getItem().getOwner().getId(),
                        bookItemResponseDto.getId(), false));
        assertEquals(badRequestException.getMessage(), "Status already updated.");
    }

    @Test
    void approveBooking_whenBookingIsFoundAndUserNotOwnerToItemAndItemStatusIsWaitingAndApprovedIsFalse_ThenNotFoundExceptionThrown() {
        Item item = new Item(0L, "itemName", "itemDescription",
                false, owner, request);
        Booking bookingToSave = new Booking(0L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5),
                item, requestor, "WAITING");

        BookItemResponseDto bookItemResponseDto = BookingMapper.toBookingDto(bookingToSave);
        when(bookingRepository.findById(bookItemResponseDto.getId()))
                .thenReturn(Optional.of(bookingToSave));

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(wrongOwner.getId(),
                        bookItemResponseDto.getId(), false));
        assertEquals(notFoundException.getMessage(), "The user is not the owner of the item.");
    }

    @Test
    void getAllBookingsByUserId_whenUserIdNotCorrectAndStatusDtoIsAllAndFromIsNullAndSizeIsNull_thenNotfoundExceptionThrown() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.ALL, null, null));
        assertEquals(notFoundException.getMessage(), "User not found.");
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsAllAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size - 1, sort);
        when(bookingRepository.findByBookerId(0L, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.ALL, from, size);

        verify(bookingRepository, times(1)).findByBookerId(0L, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsCurrentAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.CURRENT, from, size);

        verify(bookingRepository, times(1)).findByBooker_IdAndStartIsBeforeAndEndIsAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Pageable.class));
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsPastAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByBooker_IdAndEndIsBefore(anyLong(),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.PAST, from, size);

        verify(bookingRepository, times(1)).findByBooker_IdAndEndIsBefore(anyLong(), any(LocalDateTime.class),
                any(Pageable.class));
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsFutureAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByBooker_IdAndStartIsAfter(anyLong(),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.FUTURE, from, size);

        verify(bookingRepository, times(1)).findByBooker_IdAndStartIsAfter(anyLong(), any(LocalDateTime.class),
                any(Pageable.class));
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsWaitingAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByBookerIdAndStatus(0L, Booking.Status.WAITING, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.WAITING, from, size);

        verify(bookingRepository, times(1)).findByBookerIdAndStatus(
                0L, Booking.Status.WAITING, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsApprovedAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByBookerIdAndStatus(0L, Booking.Status.APPROVED, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.APPROVED, from, size);

        verify(bookingRepository, times(1)).findByBookerIdAndStatus(
                0L, Booking.Status.APPROVED, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsRejectedAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByBookerIdAndStatus(0L, Booking.Status.REJECTED, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.REJECTED, from, size);

        verify(bookingRepository, times(1)).findByBookerIdAndStatus(
                0L, Booking.Status.REJECTED, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByUserId_whenUserIdIsCorrectAndStatusDtoIsCanceledAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByBookerIdAndStatus(0L, Booking.Status.CANCELED, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByUserId(0L, BookItemResponseDto.StatusDto.CANCELED, from, size);

        verify(bookingRepository, times(1)).findByBookerIdAndStatus(
                0L, Booking.Status.CANCELED, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdNotCorrectAndStatusDtoIsAllAndFromIsNullAndSizeIsNull_thenNotfoundExceptionThrown() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.ALL, null, null));
        assertEquals(notFoundException.getMessage(), "User not correct.");
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsAllAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByOwnerId(0L, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.ALL, from, size);

        verify(bookingRepository, times(1)).findByOwnerId(0L, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsCurrentAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByOwnerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.CURRENT, from, size);

        verify(bookingRepository, times(1)).findByOwnerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Pageable.class));
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsPastAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByOwnerIdAndEndBefore(anyLong(),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.PAST, from, size);

        verify(bookingRepository, times(1)).findByOwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class),
                any(Pageable.class));
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsFutureAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findByOwnerIdAndSAndStartIsAfter(anyLong(),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.FUTURE, from, size);

        verify(bookingRepository, times(1)).findByOwnerIdAndSAndStartIsAfter(anyLong(), any(LocalDateTime.class),
                any(Pageable.class));
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsWaitingAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByOwnerIdAndStatus(0L, Booking.Status.WAITING, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.WAITING, from, size);

        verify(bookingRepository, times(1)).findByOwnerIdAndStatus(
                0L, Booking.Status.WAITING, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsApprovedAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByOwnerIdAndStatus(0L, Booking.Status.APPROVED, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.APPROVED, from, size);

        verify(bookingRepository, times(1)).findByOwnerIdAndStatus(
                0L, Booking.Status.APPROVED, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsRejectedAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByOwnerIdAndStatus(0L, Booking.Status.REJECTED, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.REJECTED, from, size);

        verify(bookingRepository, times(1)).findByOwnerIdAndStatus(
                0L, Booking.Status.REJECTED, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }

    @Test
    void getAllBookingsByOwnerId_whenUserIdIsCorrectAndStatusDtoIsCanceledAndFromNotNullAndSizeNotNull_thenReturnList() {
        int from = 0;
        int size = 4;
        List<Booking> exceptedList = new ArrayList<>(bookingList);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(from, size, sort);
        when(bookingRepository.findByOwnerIdAndStatus(0L, Booking.Status.CANCELED, page))
                .thenReturn(exceptedList);

        bookingService.getAllBookingsByOwnerId(0L, BookItemResponseDto.StatusDto.CANCELED, from, size);

        verify(bookingRepository, times(1)).findByOwnerIdAndStatus(
                0L, Booking.Status.CANCELED, page);
        assertEquals(exceptedList.get(0), bookingList.get(0));
        assertEquals(exceptedList.get(1), bookingList.get(1));
    }
}