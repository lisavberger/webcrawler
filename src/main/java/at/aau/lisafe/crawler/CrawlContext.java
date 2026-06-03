package at.aau.lisafe.crawler;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.aau.lisafe.util.CrawlerUtils;

/**
 * Tracks the state of an in-progress crawl.
 *
 * The CrawlContext holds:
 * - a thread-safe queue of {@link CrawlRequest}s that are waiting to be
 *   processed,
 * - the set of URLs that have already been queued, used to deduplicate work
 *   and avoid cycles.
 *
 * It also enforces the configured maximum crawl depth and the list of
 * allowed domains: only URLs that pass both checks are added to the queue.
 */
public class CrawlContext {
    private final Queue<CrawlRequest> pendingRequests = new ConcurrentLinkedQueue<>();
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();

    private final int maxDepth;
    private final List<String> allowedDomains;

    public CrawlContext(int maxDepth, List<String> allowedDomains) {
        this.maxDepth = maxDepth;
        this.allowedDomains = List.copyOf(allowedDomains);
    }

    /**
     * Adds the starting URL to the crawl context if it is allowed and has not
     * been visited.
     *
     * @param startUrl the URL to start crawling from
     */
    public void addStartUrl(String startUrl) {
        addIfAllowed(new CrawlRequest(startUrl, 0, null));
    }

    /**
     * Adds child URLs from a PageCrawlResult to the crawl context if they are
     * allowed and have not been visited.
     *
     * @param pageCrawlResult the result of crawling a page, containing the URL,
     *                        depth, parent URL, and linked URLs
     */
    public void addChildUrls(PageCrawlResult pageCrawlResult) {
        if (pageCrawlResult == null || pageCrawlResult.broken()) {
            return;
        }

        if (pageCrawlResult.depth() >= maxDepth) {
            return;
        }

        for (String link : pageCrawlResult.links()) {
            addIfAllowed(new CrawlRequest(link, pageCrawlResult.depth() + 1, pageCrawlResult.url()));
        }
    }

    /**
     * Polls the next CrawlRequest from the pending requests queue.
     *
     * @return the next CrawlRequest to be processed, or null if there are no
     *         pending requests
     */
    public CrawlRequest poll() {
        return pendingRequests.poll();
    }

    /**
     * Checks if there are any pending requests in the crawl context.
     *
     * @return true if there are pending requests, false otherwise
     */
    public boolean hasPendingRequests() {
        return !pendingRequests.isEmpty();
    }

    private void addIfAllowed(CrawlRequest crawlRequest) {
        if (crawlRequest == null || crawlRequest.url() == null || crawlRequest.url().isBlank()) {
            return;
        }

        if (crawlRequest.depth() > maxDepth) {
            return;
        }

        if (!CrawlerUtils.isAllowedDomain(crawlRequest.url(), allowedDomains)) {
            return;
        }

        if (visitedUrls.add(crawlRequest.url())) {
            pendingRequests.add(crawlRequest);
        }
    }
}
