#!/bin/sh

curl -XPOST 'http://localhost:9200/g1/block/_search?pretty' -d '
   {
     "size": 0,
      "aggs": {
        "blocksByIssuer": {
          "terms": {
            "field": "issuer",
            "size": 0
          },
           "aggs" : {
               "difficulty_stats" : {
                   "stats" : {
                        "field" : "powMin"
                   }
               }
           }
        }
      }
   }'
