package at.aau.lisafe;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Utility methods used by the crawler.
 *
 * This class contains helper functions for tasks such as:
 * - validating whether a URL belongs to an allowed domain
 * - parsing site names from URLs
 */
public class CrawlerUtils {

    /**
     * Checks whether a given URL belongs to one of the allowed domains.
     *
     * The method extracts the host from the URL and verifies that it
     * either exactly matches an allowed domain or is a subdomain of it.
     *
     * Examples:
     * allowed domain: example.com
     * allowed URLs:
     * example.com
     * www.example.com
     * sub.example.com
     *
     * rejected URLs:
     * anotherdomain.com
     * example.anotherdomain.com
     *
     * @param url            URL to validate
     * @param allowedDomains list of allowed domains
     * @return true if the URL belongs to an allowed domain
     */
    public static boolean isAllowedDomain(String url, List<String> allowedDomains) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            URI uri = new URI(url);
            String host = uri.getHost();

            if (host == null || host.isBlank()) {
                return false;
            }

            host = host.toLowerCase();
            for (String allowedDomain : allowedDomains) {
                String allowed = allowedDomain.trim().toLowerCase();

                if (host.equals(allowed)) {
                    return true;
                }

                if (host.endsWith("." + allowed)) {
                    return true;
                }
            }
            return false;
        } catch (IllegalArgumentException | URISyntaxException e) {
            System.err.println("ERROR: The url you are trying to fetch is invalid: " + url);
            System.err.println("Reason: " + e.getMessage());
            return false;
        }

    }

    /**
     * Extracts the site name from a given URL.
     * The method retrieves the host from the URL and returns the first segment before the dot. 
     * For example, for "https://www.example.com/page1", it will return "example". 
     * For "https://sub.example.com/page2", it will return "sub".
     * @param url
     * @return hostname of the URL or null if the URL is invalid
     */
    public static String extractSiteName(String url) {

        if (url == null || url.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(url);
            String host = uri.getHost();

            if (host == null) {
                return null;
            }

            host = host.toLowerCase();

            if (host.startsWith("www.")) {
                host = host.substring(4);
            }

            int firstDot = host.indexOf('.');
            if (firstDot > 0) {
                return host.substring(0, firstDot);
            }

            return host;

        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: The url you are trying to fetch is invalid: " + url);
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }

    private CrawlerUtils() {
        // Private constructor to prevent instantiation
    }
}
