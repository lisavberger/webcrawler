package at.aau.lisafe.visitor;

import java.util.List;
import java.util.Set;

/**
 * Represents the content extracted from a web page.
 *
 * @param headings the list of headings found on the page
 * @param links    the set of links found on the page
 */
public record PageContent(
        List<String> headings,
        Set<String> links) {
}
