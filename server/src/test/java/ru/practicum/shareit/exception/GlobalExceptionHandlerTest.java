package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void handleNotFoundException() {
        NotFoundException ex = new NotFoundException("Not found");
        var response = exceptionHandler.handleNotFoundException(ex);

        assertEquals("Not found", response.error());
    }

    @Test
    void handleValidationException() {
        ItemValidationException ex = new ItemValidationException("Validation error");
        var response = exceptionHandler.handleItemValidationException(ex);

        assertEquals("Validation error", response.error());
    }

    @Test
    void handleAccessDenied() {
        ItemAccessDeniedException ex = new ItemAccessDeniedException("Access denied");
        var response = exceptionHandler.handleItemAccessDeniedException(ex);

        assertEquals("Access denied", response.error());
    }
}