#!/bin/sh
curl -XPOST 'http://localhost:9200/gtest/movement/_search?pretty' -d '
   {
     "size": 1000,
     "query": {
        "bool": {
            "filter": [
                {"term": {"recipient" : "5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of" }},
                {"terms": {"code" : ["MEMBER_JOIN","MEMBER_ACTIVE","MEMBER_LEAVE","MEMBER_EXCLUDE","MEMBER_REVOKE"] }}
            ]
        }
     },
      "aggs": {
         "tx": {
           "range": {
             "field" : "medianTime",
             "ranges" : [
                 { "from" : 0, "to" : 1492041600 }
             ]
           },
            "aggs" : {
                "received" : {
                    "filter": {"term": {"recipient": "5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of"}},
                    "aggs": {
                      "received_stats": {
                        "stats": {
                          "field": "amount"
                        }
                      }
                    }
                }
            }
         }
       }
   }'

