package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentService {

    CommentDto create(Long userId, Long itemId, CommentDto commentDtoDto);

    List<Comment> findByItemId(Long item);

}
