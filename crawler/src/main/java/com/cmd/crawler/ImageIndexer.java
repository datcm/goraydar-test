package com.cmd.crawler;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class ImageIndexer {
    private SolrClient solrClient;

    public ImageIndexer(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public void index(ImageData imageData) {
        try {
            SolrInputDocument inputDoc = new SolrInputDocument();
            inputDoc.setField("url", imageData.url);
            inputDoc.setField("title", imageData.title);
            inputDoc.setField("description", imageData.description);
            inputDoc.addField("tags", imageData.tags);
            solrClient.add(inputDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
