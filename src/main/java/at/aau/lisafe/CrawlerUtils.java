package at.aau.lisafe;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

public class CrawlerUtils {
    public static CrawlerResult crawl(String url, int depth, List<String> domains, Set<String> visitedUrls,
            PageVisitor visitor) {
        if (visitedUrls.contains(url)) {
            return null;
        }

        if (!isAllowedDomain(url, domains)) {
            System.out.println("Skipping URL (not in allowed domains): " + url);
            return null;
        }

        visitedUrls.add(url);

        try {
            Document webpage = visitor.fetch(url);

            String indent = "  ".repeat(depth);

            System.out.println(indent + "Fetched page title: " + webpage.title());

            List<String> headings = visitor.extractHeadings(webpage);

            CrawlerResult webCrawlerResult = new CrawlerResult(url, false, headings, new ArrayList<>());

            if (depth <= 0) {
                return webCrawlerResult;
            }

            Set<String> links = visitor.extractLinks(webpage);

            for (String link : links) {
                CrawlerResult linkedResult = crawl(link, depth - 1, domains, visitedUrls, visitor);
                if (linkedResult != null) {
                    webCrawlerResult.addLinkedPage(linkedResult);
                }
            }

            return webCrawlerResult;
        } catch (IOException e) {
            String indent = "  ".repeat(depth);
            System.err.println(indent + "ERROR: Could not fetch following url: " + url);
            System.err.println(indent + "Reason: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            String indent = "  ".repeat(depth);
            System.err.println(indent + "ERROR: The url you are trying to fetch is invalid: " + url);
            System.err.println(indent + "Reason: " + e.getMessage());
            return null;
        } catch (Exception e) {
            String indent = "  ".repeat(depth);
            System.err.println(indent + "ERROR: Something went wrong in Web Crawler Execution");
            System.err.println(indent + "Reason: " + e.getMessage());
            return null;
        }
    }

    public static boolean isAllowedDomain(String url, List<String> allowedDomains) {
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
            return false;
        } catch (IllegalArgumentException | URISyntaxException e) {
            System.err.println("ERROR: The url you are trying to fetch is invalid: " + url);
            System.err.println("Reason: " + e.getMessage());
            return false;
        }

    }

    public static String extractSiteName(String url) {

        if (url == null || url.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(url);
            String host = uri.getHost(); 

            if (host == null) {
                return null;
            }

            host = host.toLowerCase();

            if (host.startsWith("www.")) {
                host = host.substring(4);
            }

            int firstDot = host.indexOf('.');
            if (firstDot > 0) {
                return host.substring(0, firstDot);
            }

            return host;

        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: The url you are trying to fetch is invalid: " + url);
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }
}
