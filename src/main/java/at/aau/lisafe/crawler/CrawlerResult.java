package at.aau.lisafe.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the result of a crawled webpage.
 *
 * Each result stores:
 * - the page URL
 * - whether the page is broken
 * - extracted headings from the page
 * - recursively crawled linked pages
 * - any error encountered during crawling
 * 
 * This class is used to build a tree-like structure of the crawled website,
 * which can then be converted to Markdown or other formats.
 */
public class CrawlerResult {
    private final String url;
    private final boolean broken;
    private final List<String> headings;
    private final List<CrawlerResult> linkedPages;
    private final CrawlerError error;

    public CrawlerResult(String url, boolean broken, List<String> headings, List<CrawlerResult> linkedPages,
            CrawlerError error) {
        this.url = url;
        this.broken = broken;
        this.headings = headings;
        this.linkedPages = linkedPages;
        this.error = error;
    }

    public static CrawlerResult broken(String url, CrawlerError error) {
        return new CrawlerResult(url, true, List.of(), new ArrayList<>(), error);
    }

    public String getUrl() {
        return url;
    }

    public boolean isBroken() {
        return broken;
    }

    public CrawlerError getError() {
        return error;
    }

    public List<String> getHeadings() {
        return Collections.unmodifiableList(headings);
    }

    public List<CrawlerResult> getLinkedPages() {
        return Collections.unmodifiableList(linkedPages);
    }

    /**
     * Adds a crawled linked page to this result.
     *
     * @param linkedPage the result of the linked page crawl
     */
    public void addLinkedPage(CrawlerResult linkedPage) {
        this.linkedPages.add(linkedPage);
    }

}
