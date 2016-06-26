package com.cmd.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by caomanhdat on 6/26/16.
 */
public abstract class ImageDataParserFactory {

    public abstract ImageData parse(Document doc) throws ParserException;

    public static ImageDataParserFactory create(String href) {
        if (href.startsWith("https://tookapic.com/")) {
            return new TookapicParserFactory();
        }
        throw new UnsupportedOperationException(href + " is not supported yet!");
    }

    private static class TookapicParserFactory extends ImageDataParserFactory{

        public ImageData parse(Document doc) throws ParserException {
            Element imgNode = doc.select("div.lightbox__wrapper img").first();
            if (imgNode == null) throw new ParserException("Cannot find img!");
            String url = imgNode.attr("data-retina-src");

            Element titleNode = doc.select("h1.u-photo-title").first();
            if (titleNode == null) throw new ParserException("Cannot find title!");
            String title = titleNode.text();

            Element descriptionNode = doc.select("div.u-photo-description").first();
            String description = "";
            if (descriptionNode != null) {
                description = descriptionNode.text();
            }


            Elements tagNodes = doc.select("div.tags > a");
            if (tagNodes == null || tagNodes.isEmpty()) throw new ParserException("Cannot find tags!");
            ArrayList<String> tags = new ArrayList<String>();
            for (Element tagNode : tagNodes) {
                tags.add(tagNode.text());
            }

            return new ImageData(url, title, description, tags.toArray(new String[tags.size()]));
        }
    }

    public static class ParserException extends Exception {
        public ParserException() {
        }

        public ParserException(String message) {
            super(message);
        }

        public ParserException(String message, Throwable cause) {
            super(message, cause);
        }

        public ParserException(Throwable cause) {
            super(cause);
        }

    }
}
