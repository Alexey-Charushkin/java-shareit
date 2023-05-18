package ru.practicum.shareit.comment.dto;

import ru.practicum.shareit.comment.model.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto commentDto) {

        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem(),
                commentDto.getAuthor(),
                commentDto.getCreated()
        );
    }
}
