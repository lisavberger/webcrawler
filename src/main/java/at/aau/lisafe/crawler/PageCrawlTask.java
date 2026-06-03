package at.aau.lisafe.crawler;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import at.aau.lisafe.visitor.PageContent;
import at.aau.lisafe.visitor.PageVisitor;

/**
 * A task that represents the crawling of a single page. This task is executed
 * by the worker pool
 * and is responsible for visiting the page, extracting its content, and
 * returning a PageCrawlResult.
 *
 * The PageCrawlTask takes a CrawlRequest (which contains the URL to crawl, its
 * depth, and parent URL)
 * and a PageVisitor (which is used to fetch and parse the page). It returns a
 * PageCrawlResult that
 * contains the results of crawling the page, including any errors encountered.
 */
public class PageCrawlTask implements Callable<PageCrawlResult> {

    private final CrawlRequest crawlRequest;
    private final PageVisitor pageVisitor;

    public PageCrawlTask(CrawlRequest crawlRequest, PageVisitor pageVisitor) {
        this.crawlRequest = crawlRequest;
        this.pageVisitor = pageVisitor;
    }

    @Override
    public PageCrawlResult call() {
        try {
            PageContent content = pageVisitor.visit(crawlRequest.url());

            return new PageCrawlResult(
                    crawlRequest.url(),
                    crawlRequest.depth(),
                    crawlRequest.parentUrl(),
                    false,
                    content.headings(),
                    content.links(),
                    null);
        } catch (IOException | RuntimeException e) {
            return new PageCrawlResult(
                    crawlRequest.url(),
                    crawlRequest.depth(),
                    crawlRequest.parentUrl(),
                    true,
                    List.of(),
                    Set.of(),
                    CrawlerError.fromException(e));
        }
    }
}
