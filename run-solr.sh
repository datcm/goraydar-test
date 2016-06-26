#!/usr/bin/env bash
./solr-6.1.0/bin/solr stop -p 8983
./solr-6.1.0/bin/solr start -cloud -p 8983 -s "./solr-collection/node1/solr"
./solr-6.1.0/bin/solr stop -p 8984
./solr-6.1.0/bin/solr start -cloud -p 8984 -s "./solr-collection/node2/solr" -z localhost:9983