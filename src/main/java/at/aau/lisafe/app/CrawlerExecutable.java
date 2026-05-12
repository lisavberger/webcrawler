package at.aau.lisafe.app;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import at.aau.lisafe.crawler.CrawlerResult;
import at.aau.lisafe.crawler.SequentialCrawler;
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
 * Expected arguments:
 * <URL> <depth> <domains>
 */
public class CrawlerExecutable {
    private static final Logger LOGGER = Logger.getLogger(CrawlerExecutable.class.getName());

    public static void main(String... args) {

        if (args.length != 3) {
            LOGGER.severe("You must pass URL, depth and domains as execution parameter:");
            LOGGER.severe("java -jar web-crawler.jar <URL> <depth> <domains>");
            LOGGER.severe("Example:");
            LOGGER.severe("java -jar web-crawler.jar https://example.com 2 example.com");
            return;
        }

        String url = args[0];

        int depth;

        try {
            depth = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            LOGGER.severe("Depth must be a valid integer.");
            return;
        }

        List<String> domains = Arrays.asList(args[2].split(","));

        LOGGER.info("Starting web crawler...");
        LOGGER.info(() -> "URL: " + url);
        LOGGER.info(() -> "Depth: " + depth);
        LOGGER.info(() -> "Domains: " + domains);

        PageVisitor visitor = new JsoupPageVisitor();

        CrawlerResult crawlerResult = SequentialCrawler.crawl(url, depth, domains, visitor);

        LOGGER.info("Print Result to Markdown...");

        String markdownResult = MarkdownUtils.toMarkdown(crawlerResult);
        String siteName = CrawlerUtils.extractSiteName(url);
        String filename = "web-crawler-result.md";

        if (siteName != null) {
            siteName = siteName.replaceAll("[^a-zA-Z0-9]", "_");
            filename = siteName + "-web-crawler-result.md";
        }

        MarkdownUtils.writeMarkdownToFile(markdownResult, filename);

        LOGGER.info("Webcrawler finished...");

    }
}
