package com.cmd.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.solr.client.solrj.SolrClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class ImagesCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");
    private final ImageIndexer imageIndexer;

    public ImagesCrawler(ImageIndexer imageIndexer) {
        this.imageIndexer = imageIndexer;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && ( href.startsWith("http://tookapic.com/")
                   || href.startsWith("https://tookapic.com/") );
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if (!url.startsWith("https://tookapic.com/photos/")) return;

        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Document jsoupDoc = Jsoup.parse(htmlParseData.getHtml());
            try {
                ImageData imageData = ImageDataParserFactory.create(url).parse(jsoupDoc);
                imageIndexer.index(imageData);
            } catch (ImageDataParserFactory.ParserException e) {
                e.printStackTrace();
            }
        }
    }
}
