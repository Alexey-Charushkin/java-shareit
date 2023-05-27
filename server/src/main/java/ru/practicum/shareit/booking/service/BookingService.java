package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookItemResponseDto;

import java.util.List;

public interface BookingService {

    BookItemResponseDto create(Long userId, BookItemResponseDto bookItemResponseDto);

    BookItemResponseDto findByBookingId(Long userId, Long bookingId);

    BookItemResponseDto approveBooking(Long userId, Long bookingId, Boolean approved);

    List<BookItemResponseDto> getAllBookingsByUserId(Long userId, BookItemResponseDto.StatusDto statusDto, Integer from, Integer size);

    List<BookItemResponseDto> getAllBookingsByOwnerId(Long ownerId, BookItemResponseDto.StatusDto statusDto, Integer from, Integer size);
}
