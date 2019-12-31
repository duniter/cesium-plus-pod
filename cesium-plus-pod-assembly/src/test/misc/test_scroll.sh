#!/bin/sh

curl -XPOST 'https://g1.data.duniter.fr/page/record/_search?pretty&scroll=1m' -d '
   {
     "query":{
        "bool" : {
          "should" : {
            "range" : {
              "time" : {
                "from" : 0,
                "to" : null,
                "include_lower" : true,
                "include_upper" : true
              }
            }
          }
        }
     }
   }' > out.txt

#curl -XPOST 'http://localhost:9200/_search?scroll=1m -d 'cXVlcnlUaGVuRmV0Y2g7MzsyNTY0OTE6WUkyT25rYy1RUWFxRDFDYmNjUzlHUTsyNTY0OTA6WUkyT25rYy1RUWFxRDFDYmNjUzlHUTsyNTY0OTI6WUkyT25rYy1RUWFxRDFDYmNjUzlHUTswOw==';'