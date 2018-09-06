#!/bin/sh

echo "--- COUNT query --- "
curl -XPOST "http://127.0.0.1:9200/_search?pretty" -d'
{
  query: {
    indices : {
            "indices" : ["user", "registry", "currency"],
            "query" : { "term" : { "tags" : "gtest" } },
            "no_match_query" : { "term" : { "tags" : "gtest" } }
            }
  },
  from: 0,
  size: 100
}'


