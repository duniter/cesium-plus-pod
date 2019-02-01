#!/bin/sh


curl -XPOST 'http://localhost:9200/g1/peer/_search?pretty' -d '
{
  "size" : 1000,
  "query" : {
    "constant_score" : {
      "filter" : {
        "bool" : {
          "must" : [
           {
             "term" : {
               "api" : "BASIC_MERKLED_API"
             }
           },
           {
              "nested" : {
                "path" : "stats",
                "query" : {
                  "bool" : {
                    "filter" : [
                        {"term": {"stats.status" : "UP" }}
                    ]
                  }
                }
              }
          }
          ]
        }
      }
    }
  }
}'
