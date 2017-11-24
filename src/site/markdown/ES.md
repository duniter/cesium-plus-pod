# ElastiSearch node

## Install

### Prerequisites

#### Install Java 

 - Install Java JRE 8 or more.
 
    - Windows: see [Oracle web site](http://oracle.com/java/index.html)
    
    - Linux (Ubuntu):
 
```bash
sudo apt-get install openjdk-8-jre 
```

### Install libsodium 

[The Sodium crypto library (libsodium)](https://download.libsodium.org/doc/installation/) is a modern, easy-to-use software library for encryption, decryption, signatures, password hashing and more. 

- Get libsodium (version 1.0.11 or newer)

```bash
wget -kL https://github.com/jedisct1/libsodium/releases/download/1.0.14/libsodium-1.0.14.tar.gz
tar -xvf libsodium-1.0.14.tar.gz
```

- Installation:

```bash
cd libsodium-1.0.14
sudo apt-get install build-essential
sudo ./configure
sudo make && sudo make check
sudo make install        
```

### Install bundle (ElasticSearch + Duniter4j)  

   - Download [lastest release](https://github.com/duniter/duniter4j/releases) of file duniter4j-es-X.Y-standalone.zip
 
 - Unzip
 
```bash
unzip duniter4j-es-X.Y-standalone.zip
cd duniter4j-es-X.Y/config
```

 - Edit the configuration file `config/elasticsearch.yml`, in particular this properties:

```yml
# Your ES cluster name
cluster.name: duniter4j-elasticsearch

# Use a descriptive name for the node:
node.name: ES-NODE-1

# Set the bind address to a specific IP (IPv4 or IPv6):
network.host: 192.168.0.28

# Set a custom port for HTTP:
http.port: 9203

# Duniter node to connect with
duniter.host: g1-test.duniter.org
duniter.port: 10900

# Initial list of hosts to perform synchronization
duniter.p2p.includes.endpoints: [
   "ES_CORE_API g1-test.data.duniter.fr 443",
   "ES_USER_API g1-test.data.duniter.fr 443",
   "ES_SUBSCRIPTION_API g1-test.data.duniter.fr 443"
]

```
 
 - Launch the node
 
```bash
cd duniter4j-es-X.Y/bin
./elasticsearch
```

Output example (on [G1-test](http://g1-test.duniter.fr) currency):

```bash
$ ./elasticsearch
[2016-09-24 00:16:45,803][INFO ][node                     ] [ES-NODE-1] version[2.3.3], pid[15365], build[218bdf1/2016-05-17T15:40:04Z]
[2016-09-24 00:16:45,804][INFO ][node                     ] [ES-NODE-1] initializing ...
[2016-09-24 00:16:46,257][INFO ][plugins                  ] [ES-NODE-1] modules [reindex, lang-expression, lang-groovy], plugins [mapper-attachments, duniter4j-elasticsearch], sites [duniter4j-elasticsearch]
[2016-09-24 00:16:46,270][INFO ][env                      ] [ES-NODE-1] using [1] data paths, mounts [[/home (/dev/mapper/isw_defjaaicfj_Volume1p1)]], net usable_space [1tb], net total_space [1.7tb], spins? [possibly], types [ext4]
[2016-09-24 00:16:46,270][INFO ][env                      ] [ES-NODE-1] heap size [989.8mb], compressed ordinary object pointers [true]
[2016-09-24 00:16:47,757][INFO ][node                     ] [ES-NODE-1] initialized
[2016-09-24 00:16:47,757][INFO ][node                     ] [ES-NODE-1] starting ...
[2016-09-24 00:16:47,920][INFO ][transport                ] [ES-NODE-1] publish_address {192.168.0.5:9300}, bound_addresses {192.168.0.5:9300}
[2016-09-24 00:16:47,924][INFO ][discovery                ] [ES-NODE-1] duniter4j-elasticsearch/jdzzh_jUTbuN26Enl-9whQ
[2016-09-24 00:16:50,982][INFO ][cluster.service          ] [ES-NODE-1] detected_master {EIS-DEV}{FD0IzkxETM6tyOqzrKuVYw}{192.168.0.28}{192.168.0.28:9300}, added {{EIS-DEV}{FD0IzkxETM6tyOqzrKuVYw}{192.168.0.28}{192.168.0.28:9300},}, reason: zen-disco-receive(from master [{EIS-DEV}{FD0IzkxETM6tyOqzrKuVYw}{192.168.0.28}{192.168.0.28:9300}])
[2016-09-24 00:16:53,570][INFO ][http                     ] [ES-NODE-1] publish_address {192.168.0.5:9203}, bound_addresses {192.168.0.5:9203}
[2016-09-24 00:16:53,570][INFO ][node                     ] [ES-NODE-1] started
[2016-09-24 00:16:57,850][INFO ][node                     ] Checking Duniter indices...
[2016-09-24 00:16:57,859][INFO ][node                     ] Checking Duniter indices... [OK]
[2016-09-24 00:17:08,026][INFO ][duniter.blockchain       ] [g1-test] [g1-test.duniter.org:10900] Indexing last blocks...
[2016-09-24 00:17:08,026][INFO ][duniter.blockchain       ] [g1-test] [g1-test.duniter.org:10900] Indexing block #999 / 41282 (2%)...
[2016-09-24 00:17:08,045][INFO ][duniter.blockchain       ] [g1-test] [g1-test.duniter.org:10900] Indexing block #1998 / 41282 (4%)...
[2016-09-24 00:17:09,026][INFO ][duniter.blockchain       ] [g1-test] [g1-test.duniter.org:10900] Indexing block #2997 / 41282 (6%)...
[2016-09-24 00:17:10,057][INFO ][duniter.blockchain       ] [g1-test] [g1-test.duniter.org:10900] Indexing block #3996 / 41282 (8%)...
...
[2016-09-24 00:17:11,026][INFO ][duniter.blockchain       ] [g1-gtest] [g1-test.duniter.org:10900] Indexing block #41282 - hash [00000AAD73B0E76B870E6779CD7ACCCE175802D7867C13B5C8ED077F380548C5]
```

### Test your node

#### Using a web browser 

The following web address should works: http://localhost:9200/node/summary

#### Using Cesium

You should also be able to use your node in the [Cesium](https://github.com/duniter/cesium) application:
 
 - in the Cesium+ settings, replace the data node address;
 - check if graph and profil avatar are display correctly.  


## Request the ES node

When a blockchain currency has been indexed, you can test some fun queries :

 - get a block by number (e.g the block #0):
    
    http://localhost:9200/g1-test/block/0 -> with some additional metadata given by ES
    
    http://localhost:9200/gtest/block/0/_source -> the original JSON block
        
 - Block #125 with only hash, dividend and memberCount:
 
    http://localhost:9200/gtest/block/125/_source?_source=number,hash,dividend,membersCount
      
 - All blocks using a pubkey (or whatever):
 
    http://localhost:9200/gtest/block/_search?q=9sbUKBMvJVxtEVhC4N9zV1GFTdaempezehAmtwA8zjKQ1
       
 - All blocks with a dividend, with only some selected fields (like dividend, number, hahs).
   Note : Query executed in command line, using CURL:

```bash
curl -XGET 'http://localhost:9200/gtest/block/_search' -d '{
"query": {
        "filtered" : {
            "filter": {
                "exists" : { "field" : "dividend" }
            }
        }
    },
    "_source": ["number", "dividend", "hash", "membersCount"]
 }'
```


More documentation here :

- [a development tutorial](./development_tutorial.html) (french);
  
- [ElasticSearch official web site](http://www.elastic.co/guide/en/elasticsearch/reference/1.3/docs-get.html#get-source-filtering)
  
- [a good tutorial on ES query](http://okfnlabs.org/blog/2013/07/01/elasticsearch-query-tutorial.html) 


## Troubleshooting

### Could not find an implementation class.

Message:

```bash
java.lang.RuntimeException: java.lang.RuntimeException: Could not find an implementation class.
        at org.duniter.core.util.websocket.WebsocketClientEndpoint.<init>(WebsocketClientEndpoint.java:56)
        at org.duniter.core.client.service.bma.BlockchainRemoteServiceImpl.addNewBlockListener(BlockchainRemoteServiceImpl.java:545)
        at org.duniter.elasticsearch.service.BlockchainService.listenAndIndexNewBlock(BlockchainService.java:106)
```

Cause:

Plugin use Websocket to get notification from a Duniter nodes. The current library ([Tyrus](https://tyrus.java.net/)) is loaded throw java Service Loader, that need access to file `META-INF/services/javax.websocket.ContainerProvider` contains by Tyrus.
ElasticSearch use separated classloader, for each plugin, that disable access to META-INF resource.

Solution :

Move Tyrus libraries into elasticsearch `lib/` directory :

```bash
    cd <ES_HOME>
    mv plugins/duniter4j-elasticsearch/tyrus-*.jar lib
    mv plugins/duniter4j-elasticsearch/javax.websocket-api-*.jar lib
```