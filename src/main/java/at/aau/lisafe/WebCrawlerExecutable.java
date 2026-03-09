package at.aau.lisafe;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
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

        System.out.println("Starting web crawler...");
        System.out.println("URL: " + url);
        System.out.println("Depth: " + depth);
        System.out.println("Domains: " + domains);

        Set<String> visitedUrls = new HashSet<>();
        PageVisitor visitor = new PageVisitor();

        crawl(url, depth, domains, visitedUrls, visitor);
    }

    private static void crawl(String url, int depth, List<String> domains, Set<String> visitedUrls,
            PageVisitor visitor) {
        if (visitedUrls.contains(url)) {
            return;
        }

        if (!isAllowedDomain(url, domains)) {
            System.out.println("Skipping URL (not in allowed domains): " + url);
            return;
        }

        visitedUrls.add(url);

        try {
            Document webpage = visitor.fetch(url);

            String indent = "  ".repeat(depth);

            System.out.println(indent + "Fetched page title: " + webpage.title());

            List<String> headings = visitor.extractHeadings(webpage);
            printHeadings(headings, depth);

            if (depth <= 0) {
                return;
            }

            Set<String> links = visitor.extractLinks(webpage);
            printLinks(links, depth);

            for (String link : links) {
                crawl(link, depth - 1, domains, visitedUrls, visitor);
            }

        } catch (IOException e) {
            String indent = "  ".repeat(depth);
            System.err.println(indent + "ERROR: Could not fetch following url: " + url);
            System.err.println(indent + "Reason: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            String indent = "  ".repeat(depth);
            System.err.println(indent + "ERROR: The url you are trying to fetch is invalid: " + url);
            System.err.println(indent + "Reason: " + e.getMessage());
        } catch (Exception e) {
            String indent = "  ".repeat(depth);
            System.err.println(indent + "ERROR: Something went wrong in Web Crawler Execution");
            System.err.println(indent + "Reason: " + e.getMessage());
        }
    }

    private static void printHeadings(List<String> headings, int currentDepth) {
        String indent = "  ".repeat(currentDepth + 1);
        System.out.println(indent + "Headings (h1-h6):");

        if (headings.isEmpty()) {
            System.out.println(indent + "No Headings available on this page");
            return;
        }
        for (String heading : headings) {
            System.out.println(indent + "  - " + heading);
        }

    }

    private static void printLinks(Set<String> links, int currentDepth) {
        String indent = "  ".repeat(currentDepth + 1);
        System.out.println(indent + "Links:");

        if (links.isEmpty()) {
            System.out.println(indent + "No Links available on this page");
            return;
        }
        for (String link : links) {
            System.out.println(indent + "  - " + link);
        }

    }

    private static boolean isAllowedDomain(String url, List<String> allowedDomains) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            URI uri = new URI(url);
            String host = uri.getHost();

            if (host == null || host.isBlank()) {
                return false;
            }

            host = host.toLowerCase();
            for (String allowedDomain : allowedDomains) {
                String allowed = allowedDomain.trim().toLowerCase();

                if (host.equals(allowed)) {
                    return true;
                }

                if (host.endsWith("." + allowed)) {
                    return true;
                }
            }
            return true;
        } catch (IllegalArgumentException | URISyntaxException e) {
            System.err.println("ERROR: The url you are trying to fetch is invalid: " + url);
            System.err.println("Reason: " + e.getMessage());
            return false;
        }

    }
}
