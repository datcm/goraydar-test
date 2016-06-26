# goraydar-test

This project for Backend/Search Engineer Entrance test.
I use Java as main programming language for this project.

## Crawler

I prefer Nutch, it can make crawler more scalable. But for the limited time of the test I will use Crawler4j.

To build crawler
```
cd crawler
./build.sh
```

To run crawler
```
cd crawler
./run.sh
```

## Web application

Jetty + Servlet will be used as web application.

## Recommendation

I will use the lastest feature (streaming expression) of Solr to make recommendation based on collaborative featuring.
In basically, When an user click for recommendation I will select top photos that also be chosen by other users coresspond to currenty query.
