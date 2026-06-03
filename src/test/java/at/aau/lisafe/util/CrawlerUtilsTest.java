package at.aau.lisafe.util;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        assertFalse(CrawlerUtils.isAllowedDomain(url, allowedDomains),
                "URL with different domain should not be allowed");
    }

    @Test
    public void shouldNotAllowDifferentSubdomain() {
        String url = "https://example.anotherdomain.com/page1";

        List<String> allowedDomains = List.of("example.com");

        assertFalse(CrawlerUtils.isAllowedDomain(url, allowedDomains),
                "URL with different subdomain should not be allowed");
    }

    @Test
    public void shouldExtractFirstHostSegmentFromSimpleDomain() {
        String url = "https://example.com/page1";

        assertEquals("example", CrawlerUtils.extractFirstHostSegment(url), "First host segment should be 'example'");
    }

    @Test
    public void shouldExtractFirstHostSegmentFromSubdomain() {
        String url = "https://sub.example.com/page2";

        assertEquals("sub", CrawlerUtils.extractFirstHostSegment(url), "First host segment should be 'sub'");
    }

    @Test
    public void shouldExtractFirstHostSegmentFromNestedDomain() {
        String url = "https://example.anotherdomain.com/page3";

        assertEquals("example", CrawlerUtils.extractFirstHostSegment(url), "First host segment should be 'example'");
    }

    @Test
    public void shouldReturnNullForInvalidUrl() {
        String url = "invalid-url";

        assertNull(CrawlerUtils.extractFirstHostSegment(url), "Invalid URL should return null");
    }
}
