package at.aau.lisafe;

import java.util.List;

/**
 * Represents the result of a crawled webpage.
 *
 * Each result stores:
 * - the page URL
 * - whether the page is broken
 * - extracted headings from the page
 * - recursively crawled linked pages
 * 
 * This class is used to build a tree-like structure of the crawled website,
 * which can then be converted to Markdown or other formats.
 */
public class CrawlerResult {
    private final String url;
    private final boolean broken;
    private final List<String> headings;
    private final List<CrawlerResult> linkedPages;

    public CrawlerResult(String url, boolean broken, List<String> headings, List<CrawlerResult> linkedPages) {
        this.url = url;
        this.broken = broken;
        this.headings = headings;
        this.linkedPages = linkedPages;
    }

    public String getUrl() {
        return url;
    }

    public boolean isBroken() {
        return broken;
    }

    public List<String> getHeadings() {
        return headings;
    }

    public List<CrawlerResult> getLinkedPages() {
        return linkedPages;
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
