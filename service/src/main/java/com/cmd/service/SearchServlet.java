package com.cmd.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class SearchServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        String start = req.getParameter("start");
        if (start == null) start = "0";
        String rows = req.getParameter("rows");
        if (rows == null) rows = "20";
        try {
            writeSearchResult(resp, search(query, Integer.parseInt(start), Integer.parseInt(rows)));
        } catch (SolrServerException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
