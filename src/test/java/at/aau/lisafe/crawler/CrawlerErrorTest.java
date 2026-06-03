package at.aau.lisafe.crawler;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CrawlerErrorTest {

    @Test
    void shouldExposeMessageAndType() {
        CrawlerError error = new CrawlerError("boom", "java.io.IOException");

        assertEquals("boom", error.message());
        assertEquals("java.io.IOException", error.exceptionType());
    }

    @Test
    void shouldDeriveMessageAndTypeFromException() {
        IOException exception = new IOException("connection refused");

        CrawlerError error = CrawlerError.fromException(exception);

        assertEquals("connection refused", error.message());
        assertEquals("java.io.IOException", error.exceptionType());
    }

    @Test
    void shouldFallBackWhenExceptionMessageIsNull() {
        IOException exception = new IOException();

        CrawlerError error = CrawlerError.fromException(exception);

        assertEquals("No additional error message available.", error.message());
        assertEquals("java.io.IOException", error.exceptionType());
    }
}
