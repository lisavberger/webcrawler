package at.aau.lisafe.util;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods used by the crawler.
 *
 * This class contains helper functions for tasks such as:
 * - validating whether a URL belongs to an allowed domain
 * - parsing site names from URLs
 */
public class CrawlerUtils {
    private CrawlerUtils() {
        // Private constructor to prevent instantiation
    }

    private static final Logger LOGGER = Logger.getLogger(CrawlerUtils.class.getName());

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
            URI uri = URI.create(url);
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
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid URL: " + url, e);
            return false;
        }

    }

    /**
     * Extracts the first host segment from a given URL.
     *
     * The method retrieves the host of the URL, strips a leading {@code www.}
     * and returns the first label before the next dot. Examples:
     * {@code https://www.example.com/page1} returns {@code "example"};
     * {@code https://sub.example.com/page2} returns {@code "sub"}.
     *
     * @param url the URL to extract the first host segment from
     * @return the leading host label, or {@code null} if the URL is invalid or has
     *         no host
     */
    public static String extractFirstHostSegment(String url) {

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
            LOGGER.log(Level.WARNING, "Invalid URL: " + url, e);
            return null;
        }
    }
}
