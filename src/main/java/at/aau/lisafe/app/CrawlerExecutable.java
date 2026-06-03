package at.aau.lisafe.app;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.aau.lisafe.crawler.ConcurrentCrawler;
import at.aau.lisafe.crawler.CrawlerResult;
import at.aau.lisafe.util.CrawlerUtils;
import at.aau.lisafe.util.MarkdownUtils;
import at.aau.lisafe.visitor.JsoupPageVisitor;
import at.aau.lisafe.visitor.PageVisitor;

/**
 * Entry point of the web crawler application.
 *
 * This class reads the command line arguments (URL, depth, and allowed domains)
 * and starts the crawling process. The crawling results are then converted
 * into a Markdown report and written to a file.
 *
 * Expected arguments: {@code <URL> <depth> <domains>}
 */
public class CrawlerExecutable {
    private static final Logger LOGGER = Logger.getLogger(CrawlerExecutable.class.getName());

    private static final int DEFAULT_THREAD_COUNT = 5;
    private static final int EXPECTED_ARG_COUNT = 3;
    private static final int URL_ARG_INDEX = 0;
    private static final int DEPTH_ARG_INDEX = 1;
    private static final int DOMAINS_ARG_INDEX = 2;
    private static final String DOMAIN_SEPARATOR = ",";
    private static final String DEFAULT_RESULT_FILENAME = "web-crawler-result.md";
    private static final String RESULT_FILENAME_SUFFIX = "-web-crawler-result.md";
    private static final String FILENAME_SANITIZER_PATTERN = "[^a-zA-Z0-9]";

    public static void main(String... args) {

        if (args.length != EXPECTED_ARG_COUNT) {
            LOGGER.warning("""
                    You must pass URL, depth and domains as execution parameter:
                    java -jar web-crawler.jar <URL> <depth> <domains>
                    Example:
                    java -jar web-crawler.jar https://example.com 2 example.com""");
            return;
        }

        String url = args[URL_ARG_INDEX];

        int depth;

        try {
            depth = Integer.parseInt(args[DEPTH_ARG_INDEX]);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Depth must be a valid integer: " + args[DEPTH_ARG_INDEX], e);
            return;
        }

        List<String> domains = Arrays.asList(args[DOMAINS_ARG_INDEX].split(DOMAIN_SEPARATOR));

        LOGGER.info("Starting web crawler...");
        LOGGER.info(() -> "URL: " + url);
        LOGGER.info(() -> "Depth: " + depth);
        LOGGER.info(() -> "Domains: " + domains);

        PageVisitor visitor = new JsoupPageVisitor();

        ConcurrentCrawler crawler = new ConcurrentCrawler(visitor, DEFAULT_THREAD_COUNT, depth, domains);
        CrawlerResult crawlerResult = crawler.crawl(url);

        LOGGER.info("Print Result to Markdown...");

        String markdownResult = MarkdownUtils.toMarkdown(crawlerResult);
        String siteName = CrawlerUtils.extractFirstHostSegment(url);
        String filename = DEFAULT_RESULT_FILENAME;

        if (siteName != null) {
            siteName = siteName.replaceAll(FILENAME_SANITIZER_PATTERN, "_");
            filename = siteName + RESULT_FILENAME_SUFFIX;
        }

        try {
            MarkdownUtils.writeMarkdownToFile(markdownResult, filename);
        } catch (UncheckedIOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        LOGGER.info("Webcrawler finished...");

    }
}
