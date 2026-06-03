package at.aau.lisafe.crawler;

import java.util.List;
import java.util.Map;

import at.aau.lisafe.visitor.PageVisitor;

/**
 * A concurrent web crawler that uses multiple threads to crawl web pages.
 *
 * The ConcurrentCrawler manages the crawling process by maintaining a crawl
 * context, executing crawling tasks in parallel using a worker pool, and
 * building a result tree from the crawl results.
 */
public class ConcurrentCrawler {

    private final CrawlContext ctx;
    private final CrawlerWorkerPool workerPool;
    private final CrawlerResultTreeBuilder resultTreeBuilder;

    /**
     * Constructs a ConcurrentCrawler with the specified parameters.
     *
     * @param pageVisitor    the PageVisitor to use for extracting information from
     *                       pages
     * @param threadCount    the number of concurrent threads to use for crawling
     * @param maxDepth       the maximum depth to crawl
     * @param allowedDomains the list of allowed domains to restrict crawling
     */
    public ConcurrentCrawler(PageVisitor pageVisitor, int threadCount, int maxDepth, List<String> allowedDomains) {
        this.ctx = new CrawlContext(maxDepth, allowedDomains);
        this.workerPool = new CrawlerWorkerPool(pageVisitor, threadCount);
        this.resultTreeBuilder = new CrawlerResultTreeBuilder();
    }

    /**
     * Starts the crawling process from the specified URL.
     *
     * @param startUrl the URL to start crawling from
     * @return a CrawlerResult representing the crawled webpage and its linked pages
     */
    public CrawlerResult crawl(String startUrl) {
        ctx.addStartUrl(startUrl);
        Map<String, PageCrawlResult> crawlResults = workerPool.executeCrawling(ctx);
        return resultTreeBuilder.buildResultTree(startUrl, crawlResults);
    }
}
