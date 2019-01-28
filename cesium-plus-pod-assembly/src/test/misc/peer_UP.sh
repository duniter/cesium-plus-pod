#!/bin/sh


curl -XPOST 'http://localhost:9205/g1/peer/_search?pretty' -d '
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
                "query" : {
                  "bool" : {
                    "filter" : {
                      "term" : {
                        "stats.status" : "DOWN"
                      }
                    }
                  }
                },
                "path" : "stats"
              }
          }
          ]
        }
      }
    }
  },
  _source: ["dns", "ipv4", "ipv6", "port", "path"]
}'
