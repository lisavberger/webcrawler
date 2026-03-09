package at.aau.lisafe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

public class Crawler {
    public static CrawlerResult crawl(String url, int depth, List<String> domains, Set<String> visitedUrls,
            PageVisitor visitor) {
        if (visitedUrls.contains(url)) {
            return null;
        }

        if (!CrawlerUtils.isAllowedDomain(url, domains)) {
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
            return new CrawlerResult(
                    url,
                    true,
                    List.of(),
                    new ArrayList<>());
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
}
