package com.tfg.angel.gameswap.backend.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionsTest {

    @Test
    void testExceptions() {

        assertThrows(GSBadRequestException.class, () -> {
            throw new GSBadRequestException("error");
        });

        assertThrows(GSNotFoundException.class, () -> {
            throw new GSNotFoundException("error");
        });

        assertThrows(GSResourceNotFoundException.class, () -> {
            throw new GSResourceNotFoundException("error");
        });
    }
}
