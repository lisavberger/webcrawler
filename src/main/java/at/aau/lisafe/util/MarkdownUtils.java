package at.aau.lisafe.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

import at.aau.lisafe.crawler.CrawlerResult;

/**
 * Utility class responsible for converting crawler results into Markdown.
 *
 * The Markdown report provides a structured overview of all crawled pages,
 * including:
 * - the page URL
 * - extracted headings
 * - linked pages
 * - broken links
 *
 * The hierarchy of pages is represented using indentation based on crawl depth.
 */
public class MarkdownUtils {
    private MarkdownUtils() {
        // Private constructor to prevent instantiation
    }

    private static final String INDENT_UNIT = "  ";
    private static final int INDENT_LEVELS_PER_DEPTH = 2;
    private static final String REPORT_TITLE = "# Web Crawler Result\n\n";

    /**
     * Converts the crawler result tree into a Markdown formatted string.
     *
     * @param result root crawler result
     * @return Markdown representation of the crawl results
     */
    public static String toMarkdown(CrawlerResult result) {
        StringBuilder markdown = new StringBuilder();
        markdown.append(REPORT_TITLE);
        toMarkdownHelper(result, markdown, 0);
        return markdown.toString();
    }

    private static void toMarkdownHelper(CrawlerResult result, StringBuilder markdown, int depth) {
        if (result == null) {
            return;
        }

        String indent = INDENT_UNIT.repeat(depth);

        if (result.isBroken()) {
            appendBrokenPage(result, markdown, indent);
            return;
        }

        appendSuccessfulPage(result, markdown, indent);

        if (!result.getLinkedPages().isEmpty()) {
            markdown.append(indent)
                    .append("  - Linked Pages:\n");

            for (CrawlerResult linkedPage : result.getLinkedPages()) {
                toMarkdownHelper(linkedPage, markdown, depth + INDENT_LEVELS_PER_DEPTH);
            }
        }
    }

    private static void appendSuccessfulPage(CrawlerResult result, StringBuilder markdown, String indent) {
        markdown.append(indent)
                .append("- URL: ")
                .append(result.getUrl())
                .append("\n");

        if (!result.getHeadings().isEmpty()) {
            markdown.append(indent)
                    .append("  - Headings (h1-h6):\n");

            for (String heading : result.getHeadings()) {
                markdown.append(indent)
                        .append("    - ")
                        .append(heading)
                        .append("\n");
            }
        }
    }

    private static void appendBrokenPage(CrawlerResult result, StringBuilder markdown, String indent) {
        markdown.append(indent)
                .append("- URL: ")
                .append(result.getUrl())
                .append(" (BROKEN)\n");

        if (result.getError() != null) {
            markdown.append(indent)
                    .append("  - Error Type: ")
                    .append(result.getError().exceptionType())
                    .append("\n");

            markdown.append(indent)
                    .append("  - Error Message: ")
                    .append(result.getError().message())
                    .append("\n");
        }
    }

    /**
     * Writes the given Markdown string to a file with the specified filename.
     *
     * @param markdown the Markdown content to write
     * @param filename the name of the file to write the Markdown content to
     * @throws UncheckedIOException if the file cannot be written; the wrapped
     *                              {@link IOException} contains the underlying
     *                              cause
     */
    public static void writeMarkdownToFile(String markdown, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(markdown);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write markdown to file: " + filename, e);
        }
    }
}
