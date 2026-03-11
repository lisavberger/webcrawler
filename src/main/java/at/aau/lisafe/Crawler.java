package at.aau.lisafe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

/**
 * Recursively crawls a webpage and its linked pages up to the specified depth.
 *
 * The crawler performs the following steps:
 * - fetches the webpage content
 * - extracts headings and links
 * - filters links by allowed domains
 * - avoids revisiting already crawled URLs
 * - records broken links
 *
 * @param url starting URL
 * @param depth remaining recursion depth
 * @param allowedDomains domains that are allowed to be crawled
 * @param visitedUrls set of URLs that have already been visited
 * @param visitor component responsible for fetching and parsing webpages
 * @return a CrawlerResult representing the crawled webpage
 */
public class Crawler {

    //Overloaded as visitedUrls is only used for recursion and should not be provided by the user
    public static CrawlerResult crawl (String url, int depth, List<String> allowedDomains, PageVisitor visitor) {
        Set<String> visitedUrls = new HashSet<>();
        return crawl(url, depth, allowedDomains, visitedUrls, visitor);
    }   

    public static CrawlerResult crawl(String url, int depth, List<String> allowedDomains, Set<String> visitedUrls,
            PageVisitor visitor) {

        // Prevent crawling the same URL multiple times
        if (visitedUrls.contains(url)) {
            return null;
        }

        if (!CrawlerUtils.isAllowedDomain(url, allowedDomains)) {
            System.out.println("Skipping URL (not in allowed domains): " + url);
            return null;
        }

        visitedUrls.add(url);

        try {
            Document webpage = visitor.fetch(url);

            List<String> headings = visitor.extractHeadings(webpage);

            CrawlerResult webCrawlerResult = new CrawlerResult(url, false, headings, new ArrayList<>());

            // Stop recursion once the maximum crawl depth is reached
            if (depth <= 0) {
                return webCrawlerResult;
            }

            Set<String> links = visitor.extractLinks(webpage);

            for (String link : links) {
                CrawlerResult linkedResult = crawl(link, depth - 1, allowedDomains, visitedUrls, visitor);
                if (linkedResult != null) {
                    webCrawlerResult.addLinkedPage(linkedResult);
                }
            }

            return webCrawlerResult;
        } catch (IOException e) {
            String indent = "  ".repeat(depth);
            System.err.println(indent + "ERROR: Could not fetch following url: " + url);
            System.err.println(indent + "Reason: " + e.getMessage());

            // If the page cannot be fetched, mark it as a broken link
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
