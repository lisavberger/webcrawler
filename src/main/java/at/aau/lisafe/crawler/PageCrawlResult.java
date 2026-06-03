package at.aau.lisafe.crawler;

import java.util.List;
import java.util.Set;

/**
 * Represents the result of crawling a single page, including its URL, depth in
 * the crawl hierarchy, parent URL, whether it is broken, any error encountered,
 * extracted headings, and links found on the page.
 *
 * This record is used to store the results of crawling each page and is later
 * used to build a tree of {@link CrawlerResult} objects.
 */
public record PageCrawlResult(String url, int depth, String parentUrl, boolean broken, List<String> headings,
        Set<String> links, CrawlerError error) {

}
