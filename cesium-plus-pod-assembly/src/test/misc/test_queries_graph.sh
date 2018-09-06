#!/bin/sh
curl -XPOST 'http://gtest.data.duniter.fr:80/user/event/_search?pretty' -d '
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
     "sort" : [
       { "time" : {"order" : "desc"}}
     ],
     _source: ["code", "time"]
   }'

