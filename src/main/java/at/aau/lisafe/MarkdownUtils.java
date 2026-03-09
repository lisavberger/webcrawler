package at.aau.lisafe;

import java.io.IOException;

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

    /**
     * Converts the crawler result tree into a Markdown formatted string.
     *
     * @param result root crawler result
     * @return Markdown representation of the crawl results
     */
    public static String toMarkdown(CrawlerResult result) {
        StringBuilder markdown = new StringBuilder();
        markdown.append("# Web Crawler Result\n\n");
        toMarkdownHelper(result, markdown, 0);
        return markdown.toString();
    }

    /**
     * Recursive helper method used to build the Markdown structure.
     *
     * Each level of the crawl hierarchy increases the indentation,
     * visually representing the depth of the crawled pages.
     *
     * @param result   current crawler result
     * @param markdown StringBuilder used to build the Markdown output
     * @param depth    current recursion depth
     */
    private static void toMarkdownHelper(CrawlerResult result, StringBuilder markdown, int depth) {
        if (result == null) {
            return;
        }

        String indent = "  ".repeat(depth);
        markdown.append(indent).append("- URL: ").append(result.getUrl()).append("\n");
        markdown.append(indent).append("  - Broken: ").append(result.isBroken()).append("\n");

        if (!result.getHeadings().isEmpty()) {
            markdown.append(indent).append("  - Headings (h1-h6):\n");
            for (String heading : result.getHeadings()) {
                markdown.append(indent).append("    - ").append(heading).append("\n");
            }
        }

        if (!result.getLinkedPages().isEmpty()) {
            markdown.append(indent).append("  - Linked Pages:\n");
            for (CrawlerResult linkedPage : result.getLinkedPages()) {
                toMarkdownHelper(linkedPage, markdown, depth + 2);
            }
        }
    }


    /**
     * Writes the given Markdown string to a file with the specified filename.
     * If the file cannot be written, an error message is printed to the console.
     * 
     * @param markdown the Markdown content to write
     * @param filename the name of the file to write the Markdown content to
     */
    public static void writeMarkdownToFile(String markdown, String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write(markdown);
        } catch (IOException e) {
            System.err.println("ERROR: Could not write markdown to file: " + filename);
            System.err.println("Reason: " + e.getMessage());
        }
    }
}
