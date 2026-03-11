package at.aau.lisafe;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class CrawlerTest {

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

        CrawlerResult result = Crawler.crawl(
                "https://example.com",
                1,
                List.of("example.com"),
                visitor);

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

        CrawlerResult result = Crawler.crawl(
                "https://example.com",
                1,
                List.of("example.com"),
                visitor);

        assertNotNull(result);
        assertEquals("https://example.com", result.getUrl());
        assertEquals(1, result.getHeadings().size());
        assertEquals("Home", result.getHeadings().get(0));
        assertEquals(1, result.getLinkedPages().size());
        assertEquals("https://example.com/broken", result.getLinkedPages().get(0).getUrl());
        assertEquals(true, result.getLinkedPages().get(0).isBroken(), "Linked page should be marked as broken");
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

        CrawlerResult result = Crawler.crawl(
                "https://example.com",
                1,
                List.of("example.com"),
                visitor);

        assertNotNull(result);
        assertEquals("https://example.com", result.getUrl());
        assertEquals(1, result.getHeadings().size());
        assertEquals("Home", result.getHeadings().get(0));
        assertEquals(1, result.getLinkedPages().size(), "Only links from allowed domains should be crawled");
        assertEquals("https://example.com/about", result.getLinkedPages().get(0).getUrl());
    }
}
