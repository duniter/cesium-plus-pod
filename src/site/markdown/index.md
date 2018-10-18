# Home

Welcome to **Cesium+ pod** web site !

Cesium+ pod is used by [Duniter](http://duniter.org)'s client applications ([Cesium](https://github.com/duniter/cesium), [WotMap](https://duniter.normandie-libre.fr/wotmap/)) to store additional data **outside the blockchain** of the crypto-currency (Ğ1, Ğ1-test).

<img src="./images/logos/logo_200px.png"/>

Cesium+ pod can store: user profiles, private & encrypted messages, invitations to certify, subscription to online services (Notification by email), but also statistics on the blockchain.  

## What is Cesium+ pod ?

Cesium+ pod use an [ElastiSearch](./ES.html) for storage and full-text capabilities. 

It comes with a public RESTfull [HTTP API](./REST_API.html) to store and retrieve all this data.

The pod source code is divided in several ElasticSearch plugins: `core`, `user` and `subscription`.

