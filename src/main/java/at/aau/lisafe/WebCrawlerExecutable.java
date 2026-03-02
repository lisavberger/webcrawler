package at.aau.lisafe;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

public class WebCrawlerExecutable {
    public static void main(String... args) {
        if (args.length != 3) {
            System.out.println("You must pass URL, depth and domains as execution parameter:");
            System.out.println("java -jar web-crawler.jar <URL> <depth> <domains>");
            System.out.println("Example:");
            System.out.println("java -jar web-crawler.jar https://example.com 2 example.com");
            return;
        }

        String url = args[0];
        int depth = Integer.parseInt(args[1]);
        List<String> domains = Arrays.asList(args[2].split(","));

        try {
            PageVisitor visitor = new PageVisitor();
            Document webpage = visitor.fetch(url);

            System.out.println("Fetched page title: " + webpage.title());

            List<String> headings = visitor.extractHeadings(webpage);
            Set<String> links = visitor.extractLinks(webpage);

            printHeadings(headings);
            printLinks(links);

        } catch (IOException e) {
            System.err.println("ERROR: Could not fetch following url: " + url);
            System.err.println("Reason: " + e.getMessage());
        }catch (Exception e) {
            System.err.println("ERROR: Something went wrong in Web Crawler Execution");
            System.err.println("Reason: " + e.getMessage());
        }

        System.out.println("Starting web crawler...");
        System.out.println("URL: " + url);
        System.out.println("Depth: " + depth);
        System.out.println("Domains: " + domains);
    }

    private static void printHeadings(List<String> headings) {
        System.out.println("Headings (h1-h6):");

        if (headings.isEmpty()) {
            System.out.println("No Headings available on this page");
            return;
        }
        for (String heading : headings) {
            System.out.println("  - " + heading);
        }

    }

    private static void printLinks(Set<String> links) {
        System.out.println("Links:");

        if (links.isEmpty()) {
            System.out.println("No Links available on this page");
            return;
        }
        for (String link : links) {
            System.out.println("  - " + link);
        }

    }
}
