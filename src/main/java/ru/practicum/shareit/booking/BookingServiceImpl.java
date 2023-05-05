package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
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
            throw new BadRequestException("Available is false.");
        }

        bookingDto.setItem(item);
        bookingDto.setBooker(user);
        bookingDto.setStatus(BookingDto.Status.WAITING);
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
    public List<BookingDto> getAllBookingsByUserId(Long userId, String state) {
        List<Booking> bookings;

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found.");
        }

        switch (state) {

            case ("ALL"): {
                bookings = bookingRepository.findByBookerId(userId, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("CURRENT"): {
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("PAST"): {
                bookings = bookingRepository.findByBooker_IdAndEndIsBefore(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("FUTURE"): {
                bookings = bookingRepository.findByBooker_IdAndStartIsAfter(userId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("WAITING"): {
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.WAITING, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("APPROVED"): {
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.APPROVED, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("REJECTED"): {
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.REJECTED, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("CANCELED"): {
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Booking.Status.CANCELED, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            default: {

                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
        }                                    //Unknown state: UNSUPPORTED_STATUS


        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state) {
        List<Booking> bookings;

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("User not correct.");
        }
        switch (state) {

            case ("ALL"): {
                bookings = bookingRepository.findByOwnerId(ownerId, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("CURRENT"): {
                bookings = bookingRepository.findByOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, LocalDateTime.now(), LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case ("PAST"): {
                bookings = bookingRepository.findByOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("FUTURE"): {
                bookings = bookingRepository.findByOwnerIdAndSAndStartIsAfter(ownerId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("WAITING"): {
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.WAITING, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("APPROVED"): {
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.APPROVED, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("REJECTED"): {
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.REJECTED, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
            case ("CANCELED"): {
                bookings = bookingRepository.findByOwnerIdAndStatus(ownerId, Booking.Status.CANCELED, Sort.by(Sort.Direction.DESC,
                        "start"));
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
