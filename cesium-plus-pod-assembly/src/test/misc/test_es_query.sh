#!/bin/sh

curl -XPOST 'http://localhost:9200/g1/block/_search?pretty' -d '
   {
     "size": 0,
      "aggs": {
        "txByRange": {
          "range": {
            "field" : "medianTime",
            "ranges" : [
                { "from" : 1491955200, "to" : 1492041600 }
            ]
          },
           "aggs" : {
               "tx_stats" : {
                   "stats" : {
                        "script" : {
                            "inline" : "txcount",
                            "lang": "native"
                        }
                   }
               },
               "time" : {
                   "stats" : { "field" : "medianTime" }
               }
           }
        }
      }
   }'


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
                        "field" : "difficulty"
                   }
               }
           }
        }
      }
   }'
