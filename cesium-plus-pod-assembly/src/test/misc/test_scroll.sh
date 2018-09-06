#!/bin/sh

#curl -XPOST 'https://g1-test.data.duniter.fr/user/profile/_search?pretty' -d '
#   {
#     "query":{"bool":{"should":{"range":{"time":{"gte":0}}}}},
#     "from":0,
#     "scroll":"1m",
#     "size": 0
#   }'


#curl -XPOST 'https://g1.data.duniter.fr/subscription/execution/_search?pretty' -d '
#  {
#    "query":{"bool":{"should":{"range":{"time":{"gte":0}}}}},
#    "size": 1000,
#    "sort": "time"
#  }'


#curl -XPOST 'http://localhost:9200/user/profile/_search/scroll?scroll=1m' -d 'cXVlcnlUaGVuRmV0Y2g7Mzs4OTU6dU5jU2NMeFlRRi0xbVZGSlVxc3dndzs4OTY6dU5jU2NMeFlRRi0xbVZGSlVxc3dndzs4OTQ6dU5jU2NMeFlRRi0xbVZGSlVxc3dndzswOw=='

curl -XPOST 'http://localhost:9200/history/delete/_search?scroll=1m'

#curl -XPOST 'http://localhost:9200/history/delete/_search/scroll?scroll=1m' -d 'cXVlcnlUaGVuRmV0Y2g7Mjs3MToxNlZjRUplMVMyaW1sZERvdVU2dHZnOzcyOjE2VmNFSmUxUzJpbWxkRG91VTZ0dmc7MDs='

curl -XPOST 'http://localhost:9200/g1-test/peer/_search' -d '{
  "constant_score" : {
    "filter" : {
      "bool" : {
        "must" : [ {
          "bool" : {
            "filter" : {
              "term" : {
                "api" : "ES_USER_API"
              }
            }
          }
        }, {
          "nested" : {
            "query" : {
              "bool" : {
                "filter" : {
                  "term" : {
                    "stats.status" : "UP"
                  }
                }
              }
            },
            "path" : "stats"
          }
        } ]
      }
    }
  }
}''