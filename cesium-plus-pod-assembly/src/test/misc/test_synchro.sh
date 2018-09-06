
curl -XPOST 'https://g1.data.le-sou.org/g1/synchro/_search?pretty' -d '
   {
      "size": 0,
      "aggs": {
         "range": {
           "range": {
              "field": "time",
              "ranges": [
                {"from":0, "to": 9996178800 }
              ]
           },
           "aggs": {
             "peer": {
               "terms": {
                 "field": "peer",
                 "size": 0
               },
               "aggs" : {
                 "result": {
                   "nested": {
                      "path": "result"
                    },
                    "aggs" : {
                        "inserts" : {
                        "stats": {
                          "field" : "result.inserts"
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