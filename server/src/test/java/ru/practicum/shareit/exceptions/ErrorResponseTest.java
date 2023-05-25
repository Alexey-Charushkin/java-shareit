package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ErrorResponseTest {

    @InjectMocks
    private ErrorResponse errorResponse = new ErrorResponse("Error message");

    @Test
    void getError() {
        String message = errorResponse.getError();
        assertEquals(message, "Error message");
    }
}