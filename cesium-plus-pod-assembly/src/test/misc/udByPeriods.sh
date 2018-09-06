#!/bin/sh

curl -XPOST 'http://localhost:9200/g1/block/_search?pretty' -d '
   {
     "size": 1000,
     query: {
          filtered: {
            filter: {

              bool: {
                must: [
                  {
                    exists: {
                      field: "dividend"
                    }
                  },
                  {
                    range: {
                        medianTime: {
                        from: 1506837759, to: 201507961583
                        }
                    }
                  }
                ]
              }
            }
          }
        },
        _source: ["medianTime", "number", "dividend", "monetaryMass", "membersCount", "unitbase"],
          sort: {
            "medianTime" : "asc"
          }
   }'
