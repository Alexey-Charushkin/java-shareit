package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto create(Long userId, BookingDto bookingDto) {
        if (bookingDto.getStart() == null
                || bookingDto.getEnd() == null
                || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Date time not correct.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
        Item item = (itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found.")));
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Owner is not be booker.");
        }

        if (!item.getAvailable()) {
            throw new BadRequestException("Available false.");
        }

        bookingDto.setItem(item);
        bookingDto.setBooker(user);
        bookingDto.setStatus(BookingDto.StatusDto.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto);

        bookingRepository.save(booking);
        log.info("Booking create.");
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto findByBookingId(Long userId, Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found."));

        if (booking.getBooker().getId().equals(userId) ||
                booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);

        } else {
            throw new NotFoundException("This User is not Owner or not Booker of this Item.");
        }
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found."));
        if (booking.getStatus().equals(Booking.Status.APPROVED)) {
            throw new BadRequestException("Status already updated.");
        }
        User owner = booking.getItem().getOwner();
        if (!Objects.equals(owner.getId(), userId)) {
            log.warn("The user with id: {} is not the owner of the item", userId);
            throw new NotFoundException("The user is not the owner of the item.");
        }
        if (approved) {
            booking.setStatus(Booking.Status.APPROVED);
        } else {
            booking.setStatus(Booking.Status.REJECTED);
        }
        bookingRepository.save(booking);
        log.info("Booking status update.");
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId, BookingDto.StatusDto statusDto, Integer from, Integer size) {

        List<Booking> bookings = null;

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found.");
        }

        switch (statusDto) {

            case ALL: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size - 1, sort);
                bookings = bookingRepository.findByBookerId(userId, page);
                break;
            }
            case CURRENT: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            }
            case PAST: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByBooker_IdAndEndIsBefore(userId, LocalDateTime.now(), page);
                break;
            }
            case FUTURE: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByBooker_IdAndStartIsAfter(userId, LocalDateTime.now(), page);
                break;
            }
            case WAITING: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.WAITING, page);
                break;
            }
            case APPROVED: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.APPROVED, page);
                break;
            }
            case REJECTED: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.REJECTED, page);
                break;
            }
            case CANCELED: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.CANCELED, page);
                break;
            }
            case UNSUPPORTED_STATUS: {
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }

        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, BookingDto.StatusDto statusDto, Integer from, Integer size) {
        List<Booking> bookings;

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("User not correct.");
        }
        switch (statusDto) {

            case ALL: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerId(ownerId, page);
                break;
            }
            case CURRENT: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, LocalDateTime.now(), LocalDateTime.now(),
                        page);
                break;
            }
            case PAST: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), page);
                break;
            }
            case FUTURE: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerIdAndSAndStartIsAfter(ownerId, LocalDateTime.now(), page);
                break;
            }
            case WAITING: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.WAITING, page);
                break;
            }
            case APPROVED: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.APPROVED, page);
                break;
            }
            case REJECTED: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.REJECTED, page);
                break;
            }
            case CANCELED: {
                Sort sort = Sort.by(Sort.Direction.DESC, "start");
                Pageable page = PageRequest.of(from, size, sort);
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.CANCELED, page);
                break;
            }
            default: {
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
