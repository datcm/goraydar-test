package com.cmd.crawler;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class ImageData {
    public final String url;
    public final String title;
    public final String description;
    public final String[] tags;

    public ImageData(String url, String title, String description, String[] tags) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }
}
