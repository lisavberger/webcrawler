package at.aau.lisafe;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;

public class WebCrawlerExecutable {
    public static void main(String... args) {
        if(args.length != 3){
            System.out.println("You must pass URL, depth and domains as execution parameter:");
            System.out.println("java -jar web-crawler.jar <URL> <depth> <domains>");
            System.out.println("Example:");
            System.out.println("java -jar web-crawler.jar https://example.com 2 example.com");
            return;
        }

        String url = args[0];
        int depth = Integer.parseInt(args[1]);
        List<String> domains = Arrays.asList(args[2].split(","));

        try {
            PageVisitor visitor = new PageVisitor();
            Document doc = visitor.fetch(url);

            System.out.println("Fetched page title: " + doc.title());
        } catch (Exception e) {
            System.err.println("ERROR: Could not fetch following url: " + url);
            System.err.println("Reason: " + e.getMessage());
        }
       

        System.out.println("Starting web crawler...");
        System.out.println("URL: " + url);
        System.out.println("Depth: " + depth);
        System.out.println("Domains: " + domains);
    }
}
