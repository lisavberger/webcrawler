package at.aau.lisafe.visitor;

import java.util.List;
import java.util.Set;

/**
 * Represents the content extracted from a web page.
 *
 * The {@code headings} and {@code links} collections are defensively copied
 * into immutable views, so the resulting record is safe to share between
 * threads and cannot be mutated by callers.
 *
 * @param headings the list of headings found on the page
 * @param links    the set of links found on the page
 */
public record PageContent(List<String> headings, Set<String> links) {

    public PageContent {
        headings = List.copyOf(headings);
        links = Set.copyOf(links);
    }
}
