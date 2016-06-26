package com.cmd.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.io.SolrClientCache;
import org.apache.solr.client.solrj.io.Tuple;
import org.apache.solr.client.solrj.io.graph.GatherNodesStream;
import org.apache.solr.client.solrj.io.stream.*;
import org.apache.solr.client.solrj.io.stream.expr.StreamFactory;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class RecommendationServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long t = System.currentTimeMillis();
        try {
            String query = req.getParameter("q");
            String user = req.getParameter("user");

            String clause = "top(n=\"30\"," +
                    " sort=\"count(*) desc\"," +
                    " gatherNodes(" +
                    "  likes," +
                    "  top(n=\"30\"," +
                    "   sort=\"count(*) desc\"," +
                    "   gatherNodes(likes," +
                    "       search(likes, q=\"user:" + user + "\", fl=\"docid\", sort=\"docid asc\", qt=\"/export\")," +
                    "       walk=\"docid->docid\"," +
                    "       gather=\"user\"," +
                    "       count(*)" +
                    "   )" +
                    "  )," +
                    "  walk=\"node->user\"," +
                    "  gather=\"docid\"," +
                    "  count(*)" +
                    " )" +
                    ")";
            JsonNode jsonNode = Unirest.get("http://localhost:8983/solr/images/stream")
                    .queryString("expr", clause)
                    .asJson().getBody();
            HashSet<String> mostCoLikedDocs = new HashSet<String>();

            JSONArray tuples = jsonNode.getObject().getJSONObject("result-set").getJSONArray("docs");
            for (int i = 0; i < tuples.length(); i++) {
                JSONObject tuple = tuples.getJSONObject(i);
                if (tuple.isNull("EOF")) {
                    mostCoLikedDocs.add(tuple.getString("node"));
                }
            }

            SolrDocumentList chosenDocs = new SolrDocumentList();

            QueryResponse queryResponse = search(query, 0, 200);
            SolrDocumentList solrDocumentList = queryResponse.getResults();
            for (SolrDocument solrDocument : solrDocumentList) {
                String id = (String) solrDocument.get("id");
                if (mostCoLikedDocs.contains(id)) {
                    chosenDocs.add(solrDocument);
                }
            }
            writeSearchResult(resp, (System.currentTimeMillis() - t), chosenDocs.size(), chosenDocs);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void main(String[] args) {

    }


}
