package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.dto.LastBooking;
import ru.practicum.shareit.booking.dto.NextBooking;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentService commentService;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        Item item = ItemMapper.toItem(user, itemDto);
        itemRepository.save(item);
        log.info("Item create.");
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto) {
        Item itemToUpdate = itemRepository.getReferenceById(itemDto.getId());
        if (!itemToUpdate.getOwner().getId().equals(userId)) {
            log.warn("The user with id: {} is not the owner of the item", userId);
            throw new NotFoundException("The user is not the owner of the item.");
        }
        if (itemDto.getName() != null) itemToUpdate.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemToUpdate.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) itemToUpdate.setAvailable(itemDto.getAvailable());

        itemRepository.save(itemToUpdate);
        log.info("Item updated.");
        return ItemMapper.toItemDto(itemToUpdate);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item;
        ItemDto itemDto;

        if (itemId == null) {
            log.warn("Item id id null");
            throw new BadRequestException("Item id is null.");
        }
        item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found."));
        itemDto = ItemMapper.toItemDto(item);

        if (item.getOwner().getId().equals(userId)) {
            getBookings(itemDto);
        }
        getComments(itemDto);

        log.info("Item with id: {} found", itemId);
        return (itemDto);
    }


    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {

        List<Item> items = itemRepository.findByOwnerId(userId, Sort.by(Sort.Direction.ASC, "id"));
        List<ItemDto> itemDtos = new ArrayList<>();

        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            getBookings(itemDto);
            getComments(itemDto);
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }


    public List<ItemDto> searchItems(String query) {
        List<Item> itemList;
        try {
            log.info("Request search films, query = {}.", query);
            itemList = itemRepository.search(query);
        } catch (EntityNotFoundException e) {
            log.warn("Items not found");
            throw new NotFoundException("Item not found.");
        }
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private ItemDto getBookings(ItemDto itemDto) {
        LocalDateTime lastEnd = null;
        LocalDateTime nextStart = null;
        LastBooking lastBooking = new LastBooking();
        NextBooking nextBooking = new NextBooking();

        List<Booking> bookings = bookingRepository.findByItemIdAndStatus(itemDto.getId(),
                Booking.Status.APPROVED, Sort.by(Sort.Direction.ASC, "end"));

        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {

                if (booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())
                        || booking.getEnd().isBefore(LocalDateTime.now())) {
                    if (lastEnd == null) {
                        lastBooking.setId(booking.getId());
                        lastBooking.setBookerId(booking.getBooker().getId());
                        lastEnd = booking.getEnd();
                    }
                    if (booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())
                            || lastEnd.isBefore(booking.getEnd())) {
                        lastBooking.setId(booking.getId());
                        lastBooking.setBookerId(booking.getBooker().getId());
                        lastEnd = booking.getEnd();
                    }
                }
                if (booking.getStart().isAfter(LocalDateTime.now())) {
                    if (nextStart == null) {
                        nextBooking.setId(booking.getId());
                        nextBooking.setBookerId(booking.getBooker().getId());
                        nextStart = booking.getStart();
                    }
                    if (nextStart.isAfter(booking.getStart())) {
                        nextBooking.setId(booking.getId());
                        nextBooking.setBookerId(booking.getBooker().getId());
                        nextStart = booking.getStart();
                    }
                }
                if (lastBooking.getId() != null) itemDto.setLastBooking(lastBooking);
                if (nextBooking.getId() != null) itemDto.setNextBooking(nextBooking);
            }
        } else {
            return itemDto;
        }
        return itemDto;
    }

    private ItemDto getComments(ItemDto itemDto) {
        List<Comment> comments = commentService.findByItemId(itemDto.getId());
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());

        itemDto.setComments(commentDtos);
        return itemDto;
    }

    @Override
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
