package com.cmd.service;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class LikeServlet extends BaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String docid = req.getParameter("docid");

        if (user == null || docid == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            like(user, docid);
            incLike(docid);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private void incLike(String docid) throws SolrServerException, IOException{
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.setField("id", docid);
        HashMap incMap = new HashMap();
        incMap.put("inc", 1);
        solrDoc.setField("numLikes", incMap);
        solrClient.add("images",solrDoc);
        solrClient.commit("images");

    }

    private void like(String user, String docid) throws SolrServerException, IOException {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.setField("id", String.format("%s-%s", user, docid));
        solrDoc.setField("user", user);
        solrDoc.setField("docid", docid);
        solrClient.add("likes", solrDoc);
        solrClient.commit("likes");
    }
}
