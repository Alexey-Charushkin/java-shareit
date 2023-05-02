package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.imageio.spi.ServiceRegistry;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
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

        if (!booking.getBooker().getId().equals(userId)
        || !booking.getItem().getOwner().getId().equals(userId)) {
            throw new BadRequestException("This User is not Owner or not Booker of this Item.");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found."));
        User owner = booking.getItem().getOwner();
        if (owner.getId() != userId) {
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
    public List<BookingDto> getAllBookingsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found.");
        }
        List<Booking> bookings = bookingRepository.findByBookerId(userId, Sort.by(Sort.Direction.DESC,
                "id"));
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("User not found.");
        }
        List<Booking> bookings = bookingRepository.findByOwnerId(ownerId, Sort.by(Sort.Direction.DESC,
                "id"));
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public ItemDto update(Long userId, ItemDto itemDto) {
//        Item itemToUpdate = itemRepository.getReferenceById(itemDto.getId());
//        if (!itemToUpdate.getOwner().getId().equals(userId)) {
//            log.warn("The user with id: {} is not the owner of the item", userId);
//            throw new NotFoundException("The user is not the owner of the item.");
//        }
//        if (itemDto.getName() != null) itemToUpdate.setName(itemDto.getName());
//        if (itemDto.getDescription() != null) itemToUpdate.setDescription(itemDto.getDescription());
//        if (itemDto.getAvailable().isPresent()) itemToUpdate.setAvailable(itemDto.getAvailable().get());
//
//        itemRepository.save(itemToUpdate);
//        log.info("Item updated.");
//
//        System.out.println(itemToUpdate);
//
//        return ItemMapper.toItemDto(itemToUpdate);
//    }
//
//    @Override
//    public ItemDto getItemById(Long itemId) {
//        ItemDto item;
//        if (itemId == null) {
//            log.warn("Item id id null");
//            throw new BadRequestException("Item id is null.");
//        }
//        try {
//            item = ItemMapper.toItemDto(itemRepository.getById(itemId));
//        } catch (EntityNotFoundException e) {
//            log.warn("Item with id: {} not found", itemId);
//            throw new NotFoundException("Item not found.");
//        }
//        log.info("Item with id: {} found", itemId);
//        return (item);
//    }
//
//    @Override
//    public List<ItemDto> getAllItemsByUserId(Long userId) {
//        return itemRepository.findByOwnerId(userId).stream()
//                .map(ItemMapper::toItemDto)
//                .collect(Collectors.toList());
//    }
//
//
//    public List<ItemDto> searchItems(String query) {
//        List<Item> itemList;
//        try {
//            log.info("Request search films, query = {}.", query);
//            itemList = itemRepository.search(query);
//        } catch (EntityNotFoundException e) {
//            log.warn("Items not found");
//            throw new NotFoundException("Item not found.");
//        }
//        return itemList.stream()
//                .map(ItemMapper::toItemDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void deleteById(Long itemId) {
//        itemRepository.deleteById(itemId);
//    }
}
