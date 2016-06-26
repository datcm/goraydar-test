package com.cmd.service;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.BaseHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler handler = new ServletContextHandler();
        server.setHandler(handler);

        handler.setAttribute("solr", new CloudSolrClient("localhost:9983"));
        handler.addServlet(SearchServlet.class, "/search");
        handler.addServlet(LikeServlet.class, "/like");
        handler.addServlet(RecommendationServlet.class, "/rec");

        server.start();
    }
}
