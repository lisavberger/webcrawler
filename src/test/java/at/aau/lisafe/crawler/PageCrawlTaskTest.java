package at.aau.lisafe.crawler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import at.aau.lisafe.visitor.PageVisitor;

public class PageCrawlTaskTest {

    @Test
    void shouldReturnSuccessfulPageCrawlResult() throws Exception {
        MockPageVisitor visitor = new MockPageVisitor();

        visitor.addPage("https://example.com", """
                <html><body>
                    <h1>Home</h1>
                    <a href="https://example.com/about">About</a>
                </body></html>
                """);

        CrawlRequest request = new CrawlRequest("https://example.com", 0, null);
        PageCrawlTask task = new PageCrawlTask(request, visitor);

        PageCrawlResult result = task.call();

        assertEquals("https://example.com", result.url());
        assertEquals(0, result.depth());
        assertNull(result.parentUrl());
        assertFalse(result.broken());
        assertNull(result.error());
        assertEquals(List.of("Home"), result.headings());
        assertTrue(result.links().contains("https://example.com/about"));
    }

    @Test
    void shouldReturnBrokenPageCrawlResultWhenPageCannotBeVisited() {
        MockPageVisitor visitor = new MockPageVisitor();

        CrawlRequest request = new CrawlRequest("https://example.com/broken", 1, "https://example.com");
        PageCrawlTask task = new PageCrawlTask(request, visitor);

        PageCrawlResult result = task.call();

        assertEquals("https://example.com/broken", result.url());
        assertEquals(1, result.depth());
        assertEquals("https://example.com", result.parentUrl());
        assertTrue(result.broken());
        assertNotNull(result.error());
        assertTrue(result.headings().isEmpty());
        assertTrue(result.links().isEmpty());
    }

    @Test
    void shouldReturnBrokenPageCrawlResultWhenVisitorThrowsRuntimeException() {
        PageVisitor visitor = url -> {
            throw new IllegalStateException("unexpected");
        };

        CrawlRequest request = new CrawlRequest("https://example.com", 0, null);
        PageCrawlTask task = new PageCrawlTask(request, visitor);

        PageCrawlResult result = task.call();

        assertTrue(result.broken());
        assertNotNull(result.error());
        assertEquals("java.lang.IllegalStateException", result.error().exceptionType());
        assertEquals("unexpected", result.error().message());
    }
}