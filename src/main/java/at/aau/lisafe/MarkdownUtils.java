package at.aau.lisafe;

import java.io.IOException;

public class MarkdownUtils {
    public static String toMarkdown(CrawlerResult result) {
        StringBuilder markdown = new StringBuilder();
        markdown.append("# Web Crawler Result\n\n");
        toMarkdownHelper(result, markdown, 0);
        return markdown.toString();
    }

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

    public static void writeMarkdownToFile(String markdown, String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write(markdown);
        } catch (IOException e) {
            System.err.println("ERROR: Could not write markdown to file: " + filename);
            System.err.println("Reason: " + e.getMessage());
        }
    }
}
