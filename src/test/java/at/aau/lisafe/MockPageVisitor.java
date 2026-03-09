package at.aau.lisafe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MockPageVisitor extends PageVisitor {
    private final Map<String, String> pages = new HashMap<>();

    public void addPage(String url, String html) {
        pages.put(url, html);
    }

    @Override
    public Document fetch(String url) throws IOException {
        String html = pages.get(url);

        if (html == null) {
            throw new IOException("Page not found: " + url);
        }

        return Jsoup.parse(html, url);
    }

}
