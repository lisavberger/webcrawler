package at.aau.lisafe.crawler;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CrawlerWorkerPoolTest {

    @Test
    void shouldReturnEmptyResultMapWhenContextHasNoStartUrl() {
        MockPageVisitor visitor = new MockPageVisitor();
        CrawlerWorkerPool pool = new CrawlerWorkerPool(visitor, 2);
        CrawlContext ctx = new CrawlContext(2, List.of("example.com"));

        Map<String, PageCrawlResult> results = pool.executeCrawling(ctx);

        assertNotNull(results);
        assertTrue(results.isEmpty(), "No tasks should run when no URLs are queued");
    }

    @Test
    void shouldStillProduceResultWhenAllowedDomainListIsEmpty() {
        MockPageVisitor visitor = new MockPageVisitor();
        visitor.addPage("https://example.com", "<html><body><h1>Home</h1></body></html>");

        CrawlerWorkerPool pool = new CrawlerWorkerPool(visitor, 1);
        CrawlContext ctx = new CrawlContext(1, List.of("example.com"));
        ctx.addStartUrl("https://example.com");

        Map<String, PageCrawlResult> results = pool.executeCrawling(ctx);

        assertNotNull(results);
        assertTrue(results.containsKey("https://example.com"));
        PageCrawlResult result = results.get("https://example.com");
        assertTrue(result.headings().contains("Home"));
    }

    @Test
    void shouldStoreBrokenResultWhenVisitorFails() {
        MockPageVisitor visitor = new MockPageVisitor();
        // No page registered → MockPageVisitor.visit throws IOException
        CrawlerWorkerPool pool = new CrawlerWorkerPool(visitor, 1);
        CrawlContext ctx = new CrawlContext(0, List.of("example.com"));
        ctx.addStartUrl("https://example.com");

        Map<String, PageCrawlResult> results = pool.executeCrawling(ctx);

        assertTrue(results.containsKey("https://example.com"));
        PageCrawlResult result = results.get("https://example.com");
        assertTrue(result.broken(), "Result should be marked as broken when visitor throws");
        assertNotNull(result.error());
    }
}
