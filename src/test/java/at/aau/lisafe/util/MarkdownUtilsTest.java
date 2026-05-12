package at.aau.lisafe.util;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import at.aau.lisafe.crawler.CrawlerResult;

public class MarkdownUtilsTest {

    @Test
    public void shouldGenerateMarkdownForSinglePage() {
        CrawlerResult result = new CrawlerResult(
                "https://example.com",
                false,
                List.of("Example Domain"),
                new ArrayList<>(),
                null);

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
                new ArrayList<>(),
                null);

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
                List.of(),
                null);

        CrawlerResult parentResult = new CrawlerResult(
                "https://example.com",
                false,
                List.of("Example Domain"),
                List.of(childResult),
                null);

        String markdown = MarkdownUtils.toMarkdown(parentResult);

        assertTrue(markdown.contains("https://example.com"));
        assertTrue(markdown.contains("Example Domain"));
        assertTrue(markdown.contains("https://example.com/child"));
        assertTrue(markdown.contains("Child Page"));
    }

    @Test
    public void shouldExportMarkdownToFile(@TempDir Path tempDir) {
        CrawlerResult result = new CrawlerResult(
                "https://example.com",
                false,
                List.of("Example Domain"),
                List.of(),
                null);

        String markdown = MarkdownUtils.toMarkdown(result);
        String filename = tempDir.resolve("test-markdown.md").toString();

        MarkdownUtils.writeMarkdownToFile(markdown, filename);

        File file = new File(filename);
        assertTrue(file.exists(), "Markdown file should be created");
        file.delete();
    }
}
