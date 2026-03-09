package at.aau.lisafe;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Responsible for fetching and parsing webpages.
 *
 * This class uses the jsoup library to:
 * - download HTML pages
 * - extract headings (h1-h6)
 * - extract links from anchor elements
 */
public class PageVisitor {

    /**
     * Downloads and parses a webpage using jsoup.
     *
     * @param url URL of the webpage
     * @return parsed HTML document
     * @throws IOException if the page cannot be fetched
     */
    public Document fetch(String url) throws IOException {
        return Jsoup.connect(url)
                .followRedirects(true)
                .timeout(10000)
                .userAgent("AAU-WebCrawler/1.0 (Student Project)")
                .get();
    }

    /**
     * Extracts all headings (h1 to h6) from a webpage.
     *
     * Only the textual content of the headings is returned.
     *
     * @param document parsed HTML document
     * @return list of headings found on the page
     */
    public List<String> extractHeadings(Document webpage) {
        return webpage.select("h1, h2, h3, h4, h5, h6").eachText();
    }

    /**
     * Extracts all links from a webpage.
     *
     * Relative URLs are automatically converted into absolute URLs
     * using jsoup's base URI functionality.
     *
     * @param document parsed HTML document
     * @return set of unique absolute URLs found on the page
     */
    public Set<String> extractLinks(Document webpage) {
        Set<String> uniqueLinks = new LinkedHashSet<>();

        // Iterate over all anchor tags containing an href attribute
        for (Element aTag : webpage.select("a[href]")) {
            String absoluteLink = aTag.attr("abs:href");

            if (absoluteLink == null || absoluteLink.isBlank()) {
                continue;
            }

            int fragmentPosition = absoluteLink.indexOf("#");

            if (fragmentPosition >= 0) {
                absoluteLink = absoluteLink.substring(0, fragmentPosition);
            }

            uniqueLinks.add(absoluteLink.trim());
        }

        return uniqueLinks;
    }
}
