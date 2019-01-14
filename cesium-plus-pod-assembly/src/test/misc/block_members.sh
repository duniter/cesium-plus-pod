#!/bin/sh

curl -XPOST 'https://g1.data.le-sou.org/g1/block/_search?pretty' -d '
   {
     "size": 1000,
     "query": {
          filtered: {
            filter: {

              bool: {
                must: [
                  {
                    exists: {
                      field: "joiners"
                    }
                  },
                  {
                    range: {
                        medianTime: {
                            gt: 1506837759
                        }
                    }
                  }
                ]
              }
            }
          }
        },
        _source: ["joiners", "number"],
          sort: {
            "number" : "asc"
          }
   }'
