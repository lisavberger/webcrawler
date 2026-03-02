package at.aau.lisafe;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PageVisitor {

    public Document fetch(String url) throws IOException {
        return Jsoup.connect(url)
                .followRedirects(true)
                .timeout(10000)
                .userAgent("AAU-WebCrawler/1.0 (Student Project)")
                .get();
    }

    public List<String> extractHeadings(Document webpage) {
        return webpage.select("h1, h2, h3, h4, h5, h6").eachText();
    }

    public Set<String> extractLinks(Document webpage) {
        Set<String> uniqueLinks = new LinkedHashSet<>();

        for(Element aTag : webpage.select("a[href]")) {
            String absoluteLink = aTag.attr("abs:href");

            if(absoluteLink == null || absoluteLink.isBlank()) {
                continue;
            }

            int fragmentPosition = absoluteLink.indexOf("#");

            if(fragmentPosition >= 0) {
                absoluteLink = absoluteLink.substring(0, fragmentPosition);
            }

            uniqueLinks.add(absoluteLink.trim());
        }

        return uniqueLinks;
    }
}
