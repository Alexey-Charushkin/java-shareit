package ru.practicum.shareit.exceptions;

import java.util.function.Supplier;

public class BadRequestException extends RuntimeException  {
    public BadRequestException(String message) {
        super(message);
    }
}

