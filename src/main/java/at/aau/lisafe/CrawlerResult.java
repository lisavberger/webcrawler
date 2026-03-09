package at.aau.lisafe;

import java.util.List;

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

    public void addLinkedPage(CrawlerResult linkedPage) {
        this.linkedPages.add(linkedPage);
    }
    
}
