package at.aau.lisafe.crawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Responsible for building a tree structure of CrawlerResult objects from a
 * flat map of PageCrawlResults.
 *
 * The CrawlerResultTreeBuilder takes the results of crawling (which are stored
 * in a map where the key is the URL
 * and the value is a PageCrawlResult) and constructs a hierarchical tree of
 * CrawlerResult objects that represent
 * the structure of the crawled website. This allows for easy conversion to
 * formats like Markdown or HTML later on.
 */
public class CrawlerResultTreeBuilder {

    /**
     * Builds a hierarchical tree of CrawlerResult objects from a flat map of
     * PageCrawlResults.
     *
     * @param startUrl         the URL to start building the tree from
     * @param pageCrawlResults a map of URL to PageCrawlResult representing the
     *                         crawled pages
     * @return a CrawlerResult representing the root of the tree
     */
    public CrawlerResult buildResultTree(String startUrl, Map<String, PageCrawlResult> pageCrawlResults) {
        return buildRecursive(startUrl, pageCrawlResults, new HashSet<>());
    }

    private CrawlerResult buildRecursive(String url, Map<String, PageCrawlResult> pageCrawlResults,
            Set<String> visited) {
        if (visited.contains(url)) {
            return null; // Avoid cycles
        }
        visited.add(url);

        PageCrawlResult pageCrawlResult = pageCrawlResults.get(url);
        if (pageCrawlResult == null) {
            return null;
        }

        if (pageCrawlResult.broken()) {
            return CrawlerResult.broken(pageCrawlResult.url(), pageCrawlResult.error());
        }

        CrawlerResult crawlerResult = new CrawlerResult(
                pageCrawlResult.url(),
                pageCrawlResult.broken(),
                pageCrawlResult.headings(),
                new ArrayList<>(),
                pageCrawlResult.error());

        for (PageCrawlResult childPage : findChildren(url, pageCrawlResults)) {
            CrawlerResult childResult = buildRecursive(childPage.url(), pageCrawlResults, visited);

            if (childResult != null) {
                crawlerResult.addLinkedPage(childResult);
            }
        }

        return crawlerResult;
    }

    private List<PageCrawlResult> findChildren(String parentUrl, Map<String, PageCrawlResult> pageCrawlResults) {
        List<PageCrawlResult> children = new ArrayList<>();

        for (PageCrawlResult pageCrawlResult : pageCrawlResults.values()) {
            if (parentUrl.equals(pageCrawlResult.parentUrl())) {
                children.add(pageCrawlResult);
            }
        }
        return children;
    }
}
