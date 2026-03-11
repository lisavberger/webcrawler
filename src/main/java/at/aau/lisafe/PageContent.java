package at.aau.lisafe;

import java.util.List;
import java.util.Set;

/**
 * Represents the content extracted from a web page.
 *
 * @param headings the list of headings found on the page
 * @param links the set of links found on the page
 */
public class PageContent {
    private final List<String> headings;
    private final Set<String> links;

    public PageContent(List<String> headings, Set<String> links) {
        this.headings = headings;
        this.links = links;
    }

    public List<String> getHeadings() {
        return headings;
    }

    public Set<String> getLinks() {
        return links;
    }
    
}
