package at.aau.lisafe.visitor;

/**
 * A visitor for extracting information from web pages.
 */
public interface PageVisitor {

    /**
     * Fetches the content of the given URL and extracts headings and links.
     *
     * @param url the URL to visit
     * @return a PageContent object containing the extracted headings and links
     * @throws Exception if an error occurs while fetching or parsing the page
     */
    PageContent visit(String url) throws Exception;

}
