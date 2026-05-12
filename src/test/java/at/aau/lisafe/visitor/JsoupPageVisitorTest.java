package at.aau.lisafe.visitor;

import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class JsoupPageVisitorTest {

    @Test
    public void testHeaderExtraction() {
        JsoupPageVisitor visitor = new JsoupPageVisitor();

        String html = """
                <html>
                    <head><title>Test Page</title></head>
                    <body>
                        <h1>Main Heading</h1>
                        <h2>Subheading 1</h2>
                        <h3>Subheading 2</h3>
                        <p>This is a test page.</p>
                    </body>
                </html>
                """;

        Document document = Jsoup.parse(html);
        List<String> headings = visitor.extractHeadings(document);

        assertEquals(3, headings.size(), "There should be 3 headings extracted");
        assertEquals("Main Heading", headings.get(0), "First heading should be 'Main Heading'");
        assertEquals("Subheading 1", headings.get(1), "Second heading should be 'Subheading 1'");
        assertEquals("Subheading 2", headings.get(2), "Third heading should be 'Subheading 2'");
    }

    @Test
    public void testLinkExtraction() {
        JsoupPageVisitor visitor = new JsoupPageVisitor();
        String html = """
                <html>
                    <head><title>Test Page</title></head>
                    <body>
                       <a href="/about">About</a>
                        <a href="https://example.com/contact">Contact</a>
                    </body>
                </html>
                """;

        Document document = Jsoup.parse(html, "https://example.com");
        Set<String> links = visitor.extractLinks(document);

        assertEquals(2, links.size());
        assertTrue(links.contains("https://example.com/about"));
        assertTrue(links.contains("https://example.com/contact"));
    }

}
