package at.aau.lisafe.crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import at.aau.lisafe.visitor.PageContent;
import at.aau.lisafe.visitor.PageVisitor;

public class MockPageVisitor implements PageVisitor {
    private final Map<String, String> pages = new HashMap<>();

    public void addPage(String url, String html) {
        pages.put(url, html);
    }

    @Override
    public PageContent visit(String url) throws IOException {
        String html = pages.get(url);

        if (html == null) {
            throw new IOException("Page not found: " + url);
        }

        Document document = Jsoup.parse(html, url);
        return new PageContent(
                document.select("h1, h2, h3, h4, h5, h6").eachText(),
                document.select("a[href]").eachAttr("abs:href").stream().collect(java.util.stream.Collectors.toSet()));

    }
}
