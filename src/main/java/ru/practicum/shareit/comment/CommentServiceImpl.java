package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@EnableJpaRepositories
public class CommentServiceImpl implements CommentService {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;


    @Override
    public CommentDto create(Long userId, Long itemId, CommentDto commentDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found."));
        List<Booking> bookings = bookingRepository.findByItemId(item.getId(),
                Sort.by(Sort.Direction.ASC, "end"));

        boolean isBooker = bookings.stream()
                .anyMatch(b -> b.getBooker().equals(user) && b.getStatus().equals(Booking.Status.APPROVED) &&
                        b.getEnd().isAfter(LocalDateTime.now()));
        if (!isBooker) throw new NotFoundException("User is not booker.");

        // comment = new Comment();
//        comment.setText(text);
        commentDto.setItem(item);
        commentDto.setAuthor(user);
        commentDto.setCreated(LocalDateTime.now());

        commentRepository.save(CommentMapper.toComment(commentDto));

        return commentDto;
    }

//    @Override
//    public List<Comment> findByItemId(Long itemId, Sort sort) {
//        return null;
//    }

    @Override
    public List<Comment> findByItemId(Long itemId) {
        return commentRepository.findByItemId(itemId, Sort.by(Sort.Direction.ASC, "end"));
    }

}
