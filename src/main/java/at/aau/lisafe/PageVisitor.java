package at.aau.lisafe;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageVisitor {

    public Document fetch(String url) throws IOException {
        return Jsoup.connect(url).followRedirects(true).timeout(10000).userAgent("AAU-WebCrawler/1.0 (Student Project)").get();
    }

}
