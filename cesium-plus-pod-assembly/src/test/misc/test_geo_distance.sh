#!/bin/sh

curl -XPOST 'https://g1.data.duniter.fr/page/record/_search?pretty&_source=title' -d '
   {
     "size": 100,
     "query": {
        "bool": {
            "filter": [{
                "geo_distance": {
                    "distance": "500km",
                    "geoPoint": {
                        "lat": 47.2186371,
                        "lon": -1.5541362
                    }
                }
            }]
        }
     }
   }'

