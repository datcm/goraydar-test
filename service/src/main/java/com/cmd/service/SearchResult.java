package com.cmd.service;

import org.apache.solr.common.SolrDocumentList;

/**
 * Created by caomanhdat on 6/26/16.
 */
class SearchResult {
    public final long QTime;
    public final long numHits;
    public final SolrDocumentList docs;

    public SearchResult(long QTime, long numHits, SolrDocumentList docs) {
        this.QTime = QTime;
        this.numHits = numHits;
        this.docs = docs;
    }
}
