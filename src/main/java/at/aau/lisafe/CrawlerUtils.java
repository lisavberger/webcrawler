package at.aau.lisafe;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class CrawlerUtils {
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
}
