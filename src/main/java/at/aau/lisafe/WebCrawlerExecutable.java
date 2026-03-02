package at.aau.lisafe;

import java.util.Arrays;
import java.util.List;

public interface WebCrawlerExecutable {
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

        System.out.println("Starting web crawler...");
        System.out.println("URL: " + url);
        System.out.println("Depth: " + depth);
        System.out.println("Domains: " + domains);
    }
}
