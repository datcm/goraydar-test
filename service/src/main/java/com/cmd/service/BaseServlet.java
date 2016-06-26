package com.cmd.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class BaseServlet extends HttpServlet{
    protected SolrClient solrClient;
    static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    public void init() throws ServletException {
        solrClient = (SolrClient) getServletContext().getAttribute("solr");
    }

    protected void writeSearchResult(HttpServletResponse resp, QueryResponse queryResp) throws IOException {
        resp.setHeader("Content-Type", "application/json");
        resp.getWriter().write(GSON.toJson(
                new SearchResult(queryResp.getQTime(), queryResp.getResults().getNumFound(), queryResp.getResults())
        ));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    protected void writeSearchResult(HttpServletResponse resp, long qTime, long numFound, SolrDocumentList docs) throws IOException {
        resp.setHeader("Content-Type", "application/json");
        resp.getWriter().write(GSON.toJson(
                new SearchResult(qTime, numFound, docs)
        ));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    protected QueryResponse search(String query, int start, int rows) throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.set("defType","dismax");
        solrQuery.set("qf", "title^2.0 description tags^1.5");
        solrQuery.set("fl", "id, url, title, tags");
        solrQuery.set("start", start);
        solrQuery.set("rows", rows);
        return solrClient.query("images",solrQuery);
    }
}
