package at.aau.lisafe.visitor;

import java.io.IOException;

/**
 * A visitor for extracting information from web pages.
 */
public interface PageVisitor {

    /**
     * Fetches the content of the given URL and extracts headings and links.
     *
     * @param url the URL to visit
     * @return a PageContent object containing the extracted headings and links
     * @throws IOException           if the page cannot be fetched or parsed
     * @throws IllegalArgumentException if {@code url} is not a valid URL
     */
    PageContent visit(String url) throws IOException;

}
