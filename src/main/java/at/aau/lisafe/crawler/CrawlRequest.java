package at.aau.lisafe.crawler;

/**
 * Represents a request to crawl a specific URL, along with its depth in the
 * crawl hierarchy and the URL of its parent page.
 *
 * @param url       the URL to be crawled
 * @param depth     the depth of the URL in the crawl hierarchy (0 for the
 *                  starting URL)
 * @param parentUrl the URL of the parent page that linked to this URL (null for
 *                  the starting URL)
 */
public record CrawlRequest(String url, int depth, String parentUrl) {

}
