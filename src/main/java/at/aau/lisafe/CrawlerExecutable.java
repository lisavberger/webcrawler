package at.aau.lisafe;

import java.util.Arrays;
import java.util.List;
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
    public static void main(String... args) {
        if (args.length != 3) {
            System.err.println("You must pass URL, depth and domains as execution parameter:");
            System.err.println("java -jar web-crawler.jar <URL> <depth> <domains>");
            System.err.println("Example:");
            System.err.println("java -jar web-crawler.jar https://example.com 2 example.com");
            return;
        }

        String url = args[0];

        int depth;

        try {
            depth = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Depth must be a valid integer.");
            return;
        }
        
        
        List<String> domains = Arrays.asList(args[2].split(","));

        System.out.println("Starting web crawler...");
        System.out.println("URL: " + url);
        System.out.println("Depth: " + depth);
        System.out.println("Domains: " + domains);
        
        PageVisitor visitor = new JsoupPageVisitor();

        CrawlerResult crawlerResult = Crawler.crawl(url, depth, domains, visitor);

        System.out.println("Print Result to Markdown...");

        String markdownResult = MarkdownUtils.toMarkdown(crawlerResult);
        String siteName = CrawlerUtils.extractSiteName(url);
        String filename = "web-crawler-result.md";

        if(siteName != null) {
            siteName = siteName.replaceAll("[^a-zA-Z0-9]", "_");
            filename = siteName + "-web-crawler-result.md";
        }
       
        MarkdownUtils.writeMarkdownToFile(markdownResult, filename);

        System.out.println("Webcrawler finished...");

    }
    
}
