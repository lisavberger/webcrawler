package at.aau.lisafe.crawler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CrawlerResultTreeBuilderTest {

    @Test
    void shouldBuildTreeFromFlatPageResults() {
        Map<String, PageCrawlResult> pageResults = new ConcurrentHashMap<>();

        pageResults.put("https://example.com", new PageCrawlResult(
                "https://example.com",
                0,
                null,
                false,

                List.of("Home"),
                Set.of("https://example.com/about"), null));

        pageResults.put("https://example.com/about", new PageCrawlResult(
                "https://example.com/about",
                1,
                "https://example.com",
                false,
                List.of("About"),
                Set.of(), null));

        CrawlerResultTreeBuilder builder = new CrawlerResultTreeBuilder();

        CrawlerResult result = builder.buildResultTree("https://example.com", pageResults);

        assertNotNull(result);
        assertEquals("https://example.com", result.getUrl());
        assertEquals(List.of("Home"), result.getHeadings());
        assertEquals(1, result.getLinkedPages().size());

        CrawlerResult child = result.getLinkedPages().get(0);

        assertEquals("https://example.com/about", child.getUrl());
        assertEquals(List.of("About"), child.getHeadings());
    }

    @Test
    void shouldKeepBrokenPageInTree() {
        Map<String, PageCrawlResult> pageResults = new ConcurrentHashMap<>();

        pageResults.put("https://example.com", new PageCrawlResult(
                "https://example.com",
                0,
                null,
                false,
                List.of("Home"),
                Set.of("https://example.com/broken"),
                null));

        pageResults.put("https://example.com/broken", new PageCrawlResult(
                "https://example.com/broken",
                1,
                "https://example.com",
                true,
                List.of(),
                Set.of(),
                new CrawlerError("Page not found", "IOException")));
        CrawlerResultTreeBuilder builder = new CrawlerResultTreeBuilder();

        CrawlerResult result = builder.buildResultTree("https://example.com", pageResults);

        assertNotNull(result);
        assertEquals(1, result.getLinkedPages().size());

        CrawlerResult broken = result.getLinkedPages().get(0);

        assertEquals("https://example.com/broken", broken.getUrl());
        assertTrue(broken.isBroken());
        assertNotNull(broken.getError());
        assertEquals("IOException", broken.getError().exceptionType());
    }

    @Test
    void shouldReturnNullWhenRootIsMissing() {
        Map<String, PageCrawlResult> pageResults = new ConcurrentHashMap<>();

        CrawlerResultTreeBuilder builder = new CrawlerResultTreeBuilder();

        CrawlerResult result = builder.buildResultTree("https://example.com", pageResults);

        assertNull(result);
    }
}