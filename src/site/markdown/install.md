# Install

## Prerequisites

### Install Java 

 - Install Java JRE 8 or more.
 
    - Windows: see [Oracle web site](http://oracle.com/java/index.html)
    
    - Linux (Ubuntu):
 
```bash
sudo apt-get install openjdk-8-jre 
```

### Install libsodium 

[The Sodium crypto library (libsodium)](https://download.libsodium.org/doc/installation/) is a modern, easy-to-use software library for encryption, decryption, signatures, password hashing and more. 

- Get libsodium (version 1.0.14 or newer)

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

### Install bundle (ElasticSearch and Cesium+ Pod)  

 - Download [lastest release](https://github.com/duniter/cesium-plus-pod/releases) of file cesium-plus-pod-X.Y-standalone.zip
 
 - Unzip the archive:
 
    ```bash
    unzip cesium-plus-pod-X.Y-standalone.zip
    cd cesium-plus-pod-X.Y
    ```

 - Edit the configuration file `config/elasticsearch.yml`, in particular this properties:
 
    ```yml
    # Your ES cluster name
    cluster.name: cesium-plus-pod-g1-TEST
    
    # Your ES cluster public host name (optional - required for publishing peering document)
    cluster.remote.host: cesium-plus-pod.domain.com
    cluster.remote.port: cesium-plus-pod.domain.com
    
    # Use a descriptive name for the node:
    node.name: ES-NODE-1
    
    # Set the bind address to a specific IP (IPv4 or IPv6):
    network.host: 192.168.0.28
    
    # Set a custom port for HTTP:
    http.port: 9200
    
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
 
 - Launch the Pod:
 
    ```bash
    cd cesium-plus-pod-X.Y/bin
    ./elasticsearch
    ```
   
   Alternatively, to run as daemon:

    ```bash
    ./elasticsearch -d
    ```

You should see in console something like (example on the [G1-test](http://g1-test.duniter.fr) currency):

```bash
$ ./elasticsearch
[2016-09-24 00:16:45,803][INFO ][node                     ] [ES-NODE-1] version[2.4.6], pid[15365], build[218bdf1/2016-05-17T15:40:04Z]
[2016-09-24 00:16:45,804][INFO ][node                     ] [ES-NODE-1] initializing ...
[2016-09-24 00:16:46,257][INFO ][plugins                  ] [ES-NODE-1] modules [reindex, lang-expression, lang-groovy], plugins [mapper-attachments, cesium-plus-pod-g1-TEST], sites [cesium-plus-pod-g1-TEST]
[2016-09-24 00:16:46,270][INFO ][env                      ] [ES-NODE-1] using [1] data paths, mounts [[/home (/dev/mapper/isw_defjaaicfj_Volume1p1)]], net usable_space [1tb], net total_space [1.7tb], spins? [possibly], types [ext4]
[2016-09-24 00:16:46,270][INFO ][env                      ] [ES-NODE-1] heap size [989.8mb], compressed ordinary object pointers [true]
[2016-09-24 00:16:47,757][INFO ][node                     ] [ES-NODE-1] initialized
[2016-09-24 00:16:47,757][INFO ][node                     ] [ES-NODE-1] starting ...
[2016-09-24 00:16:47,920][INFO ][transport                ] [ES-NODE-1] publish_address {192.168.0.5:9300}, bound_addresses {192.168.0.5:9300}
[2016-09-24 00:16:47,924][INFO ][discovery                ] [ES-NODE-1] cesium-plus-pod-g1-TEST/jdzzh_jUTbuN26Enl-9whQ
[2016-09-24 00:16:50,982][INFO ][cluster.service          ] [ES-NODE-1] detected_master {EIS-DEV}{FD0IzkxETM6tyOqzrKuVYw}{192.168.0.28}{192.168.0.28:9300}, added {{EIS-DEV}{FD0IzkxETM6tyOqzrKuVYw}{192.168.0.28}{192.168.0.28:9300},}, reason: zen-disco-receive(from master [{EIS-DEV}{FD0IzkxETM6tyOqzrKuVYw}{192.168.0.28}{192.168.0.28:9300}])
[2016-09-24 00:16:53,570][INFO ][http                     ] [ES-NODE-1] publish_address {192.168.0.5:9200}, bound_addresses {192.168.0.5:9200}
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

## Test your node

### Using a web browser 

The following web address should works: http://localhost:9200/node/summary

### Using Cesium

You should also be able to use your Pod in the [Cesium](https://git.duniter.org/clients/cesium-grp/cesium) application:
 
 - In settings, enable to Cesium+ extension;
 - Replace the Pod address (e.g. `localhost:9200`) ;
 - check if profile's avatar, grapĥ, etc. are displayed correctly. 

### Request the ES node

When a blockchain currency has been indexed, you can test some fun queries :

 - get a block by number (e.g the block #0):
    
    [/blockchain/block/0](http://localhost:9200/blockchain/block/0) -> the original block
    
    [/g1-test/block/0](http://localhost:9200/g1-test/block/0) -> Same, with more fields    
        
 - Block #125 with only hash, dividend and memberCount:
 
    http://localhost:9200/g1-test/block/125/_source?_source=number,hash,dividend,membersCount
      
 - All blocks using a pubkey (or whatever):
 
    http://localhost:9200/g1-test/block/_search?q=9sbUKBMvJVxtEVhC4N9zV1GFTdaempezehAmtwA8zjKQ1
       
 - All blocks with a dividend, with only some selected fields (like dividend, number, hahs).
   Note : Query executed in command line, using CURL:

    ```bash
    curl -XGET 'http://localhost:9200/g1-test/block/_search' -d '{
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

## Update to latest version

- Download [latest stable version](https://github.com/duniter/cesium-plus-pod/releases)

- Unzip into a new directory (e.g. `/opt/cesium-plus-vX.Y.Z`)

- Stop existing pod, if running: `/bin/cesium-plus-pod stop` 
  or, if running as a `systemd` service: `sudo service cesium-plus-pod stop`
  
- From the old Pod directory: 
  * Copy the directory `data`, that contains all indexed data, into the new installation directory
  * Merge the old file `config/elasiticsearch.yml` with the new configuration file (using `diff` to compare options).
    ```bash
    cd path/to/old/cesium-plus-pod
    cp -r data /path/to/new/cesium-plus-pod
    diff config/elasticsearch.yml   /path/to/new/cesium-plus-pod/config/elasticsearch.yml
    ```

- Start the new pod: `/bin/cesium-plus-pod start`
  If was running as systemd service, __don't forget__ to update the service file, to make sure it use the new 
  installation directory;
   
- Check everething is fine, in logs:
```
tail -f /path/to/new/cesium-plus-pod/logs/<pod_cluster_name>.log
```


## More documentation

More documentation can be found here :

- Cesium+ Pod [development tutorial](./development_tutorial.html) (french);

- Cesium+ Pod [RESTfull HTTP API](./REST_API.md);
  
- [ElasticSearch official web site](http://www.elastic.co/guide/en/elasticsearch/reference/2.4/docs-get.html#get-source-filtering)
  
- [a good tutorial on ElasticSearch request API](http://okfnlabs.org/blog/2013/07/01/elasticsearch-query-tutorial.html) 


## Troubleshooting

### Error `Refused GET request to [/ws/event/user/…`

- Message:
    ```bash
    Refused GET request to [/ws/event/user/<pubkey>…
    ```

- Cause:

  Cesium try to open a WebSocket on your pod throw HTTP v1.0, and not throw HTTP v1.1.

- Solution: 

  Configure your web server to force HTTP 1.1 connection.

  On a Nginx server: 
  
   * Edit the web site configuration (usually at `/etc/nginx/site-available/<site-name>`);
   * Add this lines:
   
  ```  
    server {

        server_name pod.domain.org;
  
        (...)

        location /ws/ {
            # Replace by your Pod local address:
            proxy_pass http://127.0.0.1:9200;
  
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection 'upgrade';
            proxy_set_header Host $host;
            proxy_cache_bypass $http_upgrade;
            proxy_read_timeout 86400s;
            proxy_send_timeout 86400s;
        }
  }
  ```
    * Restart Nginx service;

### Error `Could not find an implementation class`

- Message:
    ```bash
    java.lang.RuntimeException: java.lang.RuntimeException: Could not find an implementation class.
            at org.duniter.core.util.websocket.WebsocketClientEndpoint.<init>(WebsocketClientEndpoint.java:56)
            at org.duniter.core.client.service.bma.BlockchainRemoteServiceImpl.addNewBlockListener(BlockchainRemoteServiceImpl.java:545)
            at org.duniter.elasticsearch.service.BlockchainService.listenAndIndexNewBlock(BlockchainService.java:106)
    ```

- Cause:

  Plugin use Websocket to get notification from a Duniter nodes. The current library ([Tyrus](https://tyrus.java.net/)) is loaded throw java Service Loader, that need access to file `META-INF/services/javax.websocket.ContainerProvider` contains by Tyrus.
  ElasticSearch use separated classloader, for each plugin, that disable access to META-INF resource.

- Solution :

  Move Tyrus libraries into elasticsearch `lib/` directory :

    ```bash
    cd <INSTALL_DIR>
    mv plugins/cesium-plus-pod-core/tyrus-*.jar lib
    mv plugins/cesium-plus-pod-core/javax.websocket-api-*.jar lib
    ```