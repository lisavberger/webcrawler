package at.aau.lisafe.crawler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ConcurrentCrawlerTest {
    @Test
    public void shouldCrawlOnlyUntilGivenDepth() {
        MockPageVisitor visitor = new MockPageVisitor();

        visitor.addPage("https://example.com", """
                <html><body>
                    <h1>Home</h1>
                    <a href="https://example.com/about">About</a>
                </body></html>
                """);

        visitor.addPage("https://example.com/about", """
                <html><body>
                    <h1>About</h1>
                    <a href="https://example.com/team">Team</a>
                </body></html>
                """);

        visitor.addPage("https://example.com/team", """
                <html><body>
                    <h1>Team</h1>
                </body></html>
                """);

        ConcurrentCrawler crawler = new ConcurrentCrawler(visitor, 5, 1, List.of("example.com"));

        CrawlerResult result = crawler.crawl("https://example.com");

        assertNotNull(result);
        assertEquals("https://example.com", result.getUrl());

        assertEquals(1, result.getHeadings().size());
        assertEquals("Home", result.getHeadings().get(0));

        assertEquals(1, result.getLinkedPages().size());
        assertEquals("https://example.com/about", result.getLinkedPages().get(0).getUrl());

        assertEquals(0, result.getLinkedPages().get(0).getLinkedPages().size(),
                "Linked page should not be crawled due to depth limit");
    }

    @Test
    public void shouldHandleBrokenLinks() {
        MockPageVisitor visitor = new MockPageVisitor();

        visitor.addPage("https://example.com", """
                <html><body>
                    <h1>Home</h1>
                    <a href="https://example.com/broken">Broken Link</a>
                </body></html>
                """);

        ConcurrentCrawler crawler = new ConcurrentCrawler(visitor, 5, 1, List.of("example.com"));

        CrawlerResult result = crawler.crawl("https://example.com");

        assertNotNull(result);
        assertEquals("https://example.com", result.getUrl());
        assertEquals(1, result.getLinkedPages().size());

        CrawlerResult brokenPage = result.getLinkedPages().get(0);

        assertEquals("https://example.com/broken", brokenPage.getUrl());
        assertTrue(brokenPage.isBroken(), "Linked page should be marked as broken");
        assertNotNull(brokenPage.getError(), "Broken page should contain an error");
    }

    @Test
    public void shouldRespectAllowedDomains() {
        MockPageVisitor visitor = new MockPageVisitor();

        visitor.addPage("https://example.com", """
                <html><body>
                    <h1>Home</h1>
                    <a href="https://example.com/about">About</a>
                    <a href="https://otherdomain.com/contact">Contact</a>
                </body></html>
                """);

        visitor.addPage("https://example.com/about", """
                <html><body>
                    <h1>About</h1>
                </body></html>
                """);

        ConcurrentCrawler crawler = new ConcurrentCrawler(visitor, 5, 1, List.of("example.com"));

        CrawlerResult result = crawler.crawl("https://example.com");

        assertNotNull(result);
        assertEquals("https://example.com", result.getUrl());
        assertEquals(1, result.getLinkedPages().size(), "Only links from allowed domains should be crawled");
        assertEquals("https://example.com/about", result.getLinkedPages().get(0).getUrl());
    }

    @Test
    public void shouldCrawlEachPageOnlyOnce() {
        MockPageVisitor visitor = new MockPageVisitor();

        visitor.addPage("https://example.com", """
                <html><body>
                    <h1>Home</h1>
                    <a href="https://example.com/a">A</a>
                    <a href="https://example.com/b">B</a>
                </body></html>
                """);

        visitor.addPage("https://example.com/a", """
                <html><body>
                    <h1>A</h1>
                    <a href="https://example.com/shared">Shared</a>
                </body></html>
                """);

        visitor.addPage("https://example.com/b", """
                <html><body>
                    <h1>B</h1>
                    <a href="https://example.com/shared">Shared</a>
                </body></html>
                """);

        visitor.addPage("https://example.com/shared", """
                <html><body>
                    <h1>Shared</h1>
                </body></html>
                """);

        ConcurrentCrawler crawler = new ConcurrentCrawler(visitor, 5, 2, List.of("example.com"));
        crawler.crawl("https://example.com");

        assertEquals(1, visitor.getVisitCount("https://example.com/shared"), "A page should only be crawled once");
    }
}
