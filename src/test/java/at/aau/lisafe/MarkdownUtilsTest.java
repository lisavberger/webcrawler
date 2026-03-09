package at.aau.lisafe;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MarkdownUtilsTest {

    @Test
    public void shouldGenerateMarkdownForSinglePage() {
        CrawlerResult result = new CrawlerResult(
            "https://example.com",
            false,
            List.of("Example Domain"),
            List.of()
        );

        String markdown = MarkdownUtils.toMarkdown(result);

        assertTrue(markdown.contains("https://example.com"));
        assertTrue(markdown.contains("Example Domain"));
    }
    
    @Test
    void shouldMarkBrokenLinkInMarkdown() {
        CrawlerResult result = new CrawlerResult(
                "https://example.com/broken",
                true,
                List.of(),
                new java.util.ArrayList<>()
        );

        String markdown = MarkdownUtils.toMarkdown(result);

        assertTrue(markdown.contains("Broken: true"));
        assertTrue(markdown.contains("https://example.com/broken"));
    }

    @Test
    public void shouldGenerateMarkdownForNestedPages() {
        CrawlerResult childResult = new CrawlerResult(
            "https://example.com/child",
            false,
            List.of("Child Page"),
            List.of()
        );

        CrawlerResult parentResult = new CrawlerResult(
            "https://example.com",
            false,
            List.of("Example Domain"),
            List.of(childResult)
        );

        String markdown = MarkdownUtils.toMarkdown(parentResult);

        assertTrue(markdown.contains("https://example.com"));
        assertTrue(markdown.contains("Example Domain"));
        assertTrue(markdown.contains("https://example.com/child"));
        assertTrue(markdown.contains("Child Page"));
    }

    @Test
    public void shouldExportMarkdownToFile() {
        CrawlerResult result = new CrawlerResult(
            "https://example.com",
            false,
            List.of("Example Domain"),
            List.of()
        );

        String markdown = MarkdownUtils.toMarkdown(result);
        String filename = "test-markdown.md";

        MarkdownUtils.writeMarkdownToFile(markdown, filename);

        File file = new File(filename);
        assertTrue(file.exists(), "Markdown file should be created");
        file.delete(); 
    }
}
