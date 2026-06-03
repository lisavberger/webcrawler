package at.aau.lisafe.crawler;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CrawlContextTest {

        @Test
        void shouldAddStartUrlWhenDomainIsAllowed() {
                CrawlContext ctx = new CrawlContext(
                                2,
                                List.of("example.com"));

                ctx.addStartUrl("https://example.com");

                assertTrue(ctx.hasPendingRequests());

                CrawlRequest request = ctx.poll();

                assertNotNull(request);
                assertEquals("https://example.com", request.url());
                assertEquals(0, request.depth());
                assertNull(request.parentUrl());
        }

        @Test
        void shouldNotAddStartUrlWhenDomainIsNotAllowed() {
                CrawlContext ctx = new CrawlContext(
                                2,
                                List.of("example.com"));

                ctx.addStartUrl("https://otherdomain.com");

                assertFalse(ctx.hasPendingRequests());
        }

        @Test
        void shouldAddChildUrlsWithinDepthLimit() {
                CrawlContext ctx = new CrawlContext(
                                2,
                                List.of("example.com"));

                PageCrawlResult pageResult = new PageCrawlResult(
                                "https://example.com",
                                0,
                                null,
                                false,
                                List.of("Home"),
                                Set.of("https://example.com/about"),
                                null);

                ctx.addChildUrls(pageResult);

                CrawlRequest request = ctx.poll();

                assertNotNull(request);
                assertEquals("https://example.com/about", request.url());
                assertEquals(1, request.depth());
                assertEquals("https://example.com", request.parentUrl());
        }

        @Test
        void shouldNotAddChildUrlsWhenDepthLimitIsReached() {
                CrawlContext ctx = new CrawlContext(
                                1,
                                List.of("example.com"));

                PageCrawlResult pageResult = new PageCrawlResult(
                                "https://example.com/about",
                                1,
                                "https://example.com",
                                false,
                                List.of("About"),
                                Set.of("https://example.com/team"),
                                null);

                ctx.addChildUrls(pageResult);

                assertFalse(ctx.hasPendingRequests());
        }

        @Test
        void shouldNotAddDuplicateUrls() {
                CrawlContext ctx = new CrawlContext(
                                2,
                                List.of("example.com"));

                ctx.addStartUrl("https://example.com");
                ctx.addStartUrl("https://example.com");

                assertNotNull(ctx.poll());
                assertNull(ctx.poll());
        }

        @Test
        void shouldNotAddChildUrlsFromBrokenPage() {
                CrawlContext ctx = new CrawlContext(
                                2,
                                List.of("example.com"));

                PageCrawlResult brokenResult = new PageCrawlResult(
                                "https://example.com/broken",
                                0,
                                null,
                                true,
                                List.of(),
                                Set.of("https://example.com/about"),
                                new CrawlerError("Page not found", "IOException"));

                ctx.addChildUrls(brokenResult);

                assertFalse(ctx.hasPendingRequests());
        }
}