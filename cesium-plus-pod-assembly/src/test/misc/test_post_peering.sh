#!/bin/sh

curl -XPOST 'http://localhost:9200/network/peering' -d 'Version: 10
Type: Peer
Currency: g1
PublicKey: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU
Block: 162921-000001698A08C8877FAF02A3C4547CD932765CF3994FF4747F3C1EC0EA303C7E
Endpoints:
ES_USER_API localhost 9201
ES_SUBSCRIPTION_API localhost 9201
ES_CORE_API localhost 9201
YzUtzvZEzcaKvrb5TCWnR7+J2L+AUkp9JX0EnKzbw4RstVzT4tYXMBUCfMgQm2TwkbZPk/SCnQ38aixv+CfZBQ=='