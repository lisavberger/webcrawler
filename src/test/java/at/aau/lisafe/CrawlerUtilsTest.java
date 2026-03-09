package at.aau.lisafe;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CrawlerUtilsTest {

    @Test
    public void shouldAllowExactDomain() {
        String url1 = "https://example.com/page1";
        String url2 = "https://anotherdomain.com/page3";

        List<String> allowedDomains = List.of("example.com");

        assertTrue(CrawlerUtils.isAllowedDomain(url1, allowedDomains), "URL 1 should be allowed");
        assertFalse(CrawlerUtils.isAllowedDomain(url2, allowedDomains), "URL 2 should not be allowed");
    }

    @Test
    public void shouldAllowSubdomain() {
        String url1 = "https://sub.example.com/page1";
        String url2 = "https://example.com/page2";
        String url3 = "https://example.anotherdomain.com/page3";

        List<String> allowedDomains = List.of("example.com");

        assertTrue(CrawlerUtils.isAllowedDomain(url1, allowedDomains), "URL 1 should be allowed");
        assertTrue(CrawlerUtils.isAllowedDomain(url2, allowedDomains), "URL 2 should be allowed");
        assertFalse(CrawlerUtils.isAllowedDomain(url3, allowedDomains), "URL 3 should not be allowed");
    }
    
}
