
curl -XPOST 'https://g1-test.data.duniter.fr/docstat/record/_search?pretty' -d '
   {
      "size": 0,
      "aggs": {
         "range": {
           "range": {
              "field": "time",
              "ranges": [
                {"from":1506016800, "to": 1506178800 }
              ]
           },
           "aggs": {
             "index": {
               "terms": {
                 "field": "index",
                 "size": 0
               },
               "aggs" : {
                    "type": {
                        "terms": {
                         "field": "indexType",
                         "size": 0
                       },
                       "aggs": {
                        "max" : {
                           "max" : {
                                "field" : "count"
                           }
                       }
                    }
                  }
               }
             }
           }
         }
      }
   }'