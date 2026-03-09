package at.aau.lisafe;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CrawlerUtilsTest {

    @Test
    public void shouldAllowExactDomain() {
        String url1 = "https://example.com/page1";
    
        List<String> allowedDomains = List.of("example.com");

        assertTrue(CrawlerUtils.isAllowedDomain(url1, allowedDomains), "URL 1 should be allowed");
    }

    @Test
    public void shouldAllowSubdomain() {
        String url1 = "https://sub.example.com/page1";
        String url2 = "https://example.com/page2";

        List<String> allowedDomains = List.of("example.com");

        assertTrue(CrawlerUtils.isAllowedDomain(url1, allowedDomains), "URL 1 should be allowed");
        assertTrue(CrawlerUtils.isAllowedDomain(url2, allowedDomains), "URL 2 should be allowed");
    }

    @Test
    public void shouldNotAllowInvalidUrl() {
        String url = "invalid-url";
     
        List<String> allowedDomains = List.of("example.com");
     
        assertFalse(CrawlerUtils.isAllowedDomain(url, allowedDomains), "Invalid URL should not be allowed");
    }

    @Test
    public void shouldNotAllowDifferentExactDomain() {
        String url = "https://anotherdomain.com/page1";

        List<String> allowedDomains = List.of("example.com");

        assertFalse(CrawlerUtils.isAllowedDomain(url, allowedDomains), "URL with different domain should not be allowed");
    }

    @Test
    public void shouldNotAllowDifferentSubdomain() {
        String url = "https://example.anotherdomain.com/page1";

        List<String> allowedDomains = List.of("example.com");

        assertFalse(CrawlerUtils.isAllowedDomain(url, allowedDomains), "URL with different subdomain should not be allowed");
    }

    @Test
    public void testSiteNameExtraction() {
        String url1 = "https://example.com/page1";
        String url2 = "https://sub.example.com/page2";
        String url3 = "https://example.anotherdomain.com/page3";
        String url4 = "invalid-url";

        assertTrue(CrawlerUtils.extractSiteName(url1).equals("example"), "Site name should be 'example.com'");
        assertTrue(CrawlerUtils.extractSiteName(url2).equals("sub"), "Site name should be 'sub.example.com'");
        assertTrue(CrawlerUtils.extractSiteName(url3).equals("example"), "Site name should be 'example.anotherdomain.com'");
        assertTrue(CrawlerUtils.extractSiteName(url4) == null, "Site name should be null for invalid URL");
    }
}
