# Tutoriel : Contribuer à Cesium+ Pod
## Introduction

Cet article est un tutoriel d'initiation au code source du logiciel Duniter4j.
Celui-ci vous permettra d'installer l'environnement de développement, et compiler et de lancer un noeud ElasticSearch avec le plugin Duniter4j.

A la fin de ce tutoriel, vous serez donc *capable de modifier le logiciel*. 

## Rappel d'architecture

Le projet Cesium+ Pod est composé de plusieurs sous-modules, dont des plugins ElasticSearch :

- `cesium-plus-pod-core`: Indexation de BlockChain Duniter;

- `cesium-plus-pod-user`: Indexation de données utilisateurs (profils, des messages privées, paramètres chiffrés);

- `cesium-plus-pod-subscription`: Indexation des abonnements en ligne (notifications par email);

- `cesium-plus-pod-assembly`: gestion des livrables (packaging).

## Niveau I : récupérer le code source

Ce premier niveau consiste à créer *votre propre version* des sources du logiciel et de récupérer cette copie sur votre ordinateur. Vous y produirez : 

- Votre propre compte *GitLab*

- Votre propre version du logiciel, votre *fork*

- Une copie locale des fichiers de code source provenant de votre *fork*

### Créez un compte GitHub

> Si vous disposez déjà d'un compte GitHub, vous pouvez passer cette étape.

Rendez-vous sur https://git.duniter.org (site en anglais). Renseigner les 3 champs proposés :

- Nom d'utilisateur

- E-mail

- Mot de passe

Vous recevrez probablement un e-mail de confirmation qu'il vous faudra valider. Une fois cette étape passée, vous devriez disposer d'un compte GitHub .

### Forkez le dépôt principal

Rendez-vous à l'adresse https://git.duniter.org/clients/cesium-grp/cesium-plus-pod. Cliquez sur le bouton « Fourcher » en haut de la page.

### Installer Git

L'installation de Git dépend de votre système d'exploitation. Suivez simplement les indications présentes sur : https://git-scm.com/

### Cloner votre fork

A ce stade, vous êtes en mesure de récupérer votre version du code source (votre *fork*), afin de pouvoir travailler dessus.

#### Ouvrez Git en ligne de commande

Pour récupérer le code source, lancez Git en mode console.

- Sous Linux et MacOS, ouvrez tout simplement le Terminal

- Sous Windows lancez le programme *Git Bash* :

<img src="https://forum.duniter.org/uploads/default/original/1X/6fc638dc0a22d88da7e84dbf0371e69747767f78.png" width="432" height="80"/>

#### Clonez votre fork, en ligne de commande

Retournez sur la page web GitHub, puis trouvez le bouton « Clone or download » : 
Cliquez dessus, vous pourrez alors copier l'URL de clonage en cliquant sur l'icône de valise.

Vous n'avez plus qu'à retourner dans votre console Git et saisir : 

    git clone <coller l'URL copiée>

ce qui donne dans mon cas : 

```
git clone git@git.duniter.org:clients/cesium-grp/cesium-plus-pod.git
Cloning into 'cesium-plus-pod'...
 (...)
Checking connectivity... done.
```

Si vous êtes arrivés à un comportement similaire, **bravo**, vous posséder désormais le code source du projet !

## Niveau II : Compilation et lancement

Ce second niveau vise à obtenir les outils de base pour exécuter le code source, et vérifier son bon fonctionnement. Vous y réaliserez : 

- l'installation de Java Development Kit (JDK);

- la compilation du code;

- la vérification du bon fonctionnement du code source *via* le lancement de l'application, dans un terminal.

Si l'application se lance, vous aurez dores et déjà un environnement entièrement **fonctionnel** !

### Installer JDK

- Sous Windows : Téléchargez puis installez un JDK (version 8 ou +) depuis le [site web d'Oracle](http://oracle.com/java/index.html)

- Sous Linux (Debian) : Lancez la commande suivante :

```bash
sudo apt-get install openjdk-8-jdk
```

### Installer LibSodium

[libsodium](https://download.libsodium.org/doc/index.html) est une librairie de cryptographie.

- Sous Windows : Aucune instalation nécessaire (fichier `sodium.dll` déjà présent dans la librairie `duniter4j` incluse);
 
- Sous Linux : suivre [les notes d'installation](https://download.libsodium.org/doc/installation/index.html) (anglais).

   * Après installation, vérifiez que le fichier `libsodium.so` existe bien dans `/usr/local/lib` ou `/opt/local/lib`.

   * S'il existe, mais à une autre eplacement, veuillez créez un lien symbolique à l'un ou l'autre de ces emplacements.


### Installer Apache Maven 3

Installer les outils nécessaires pour la compilation :

- Installez [Apache Maven 3](http://maven.apache.org)
   * Sous Windows : [téléchargez](http://maven.apache.org/download.cgi) (version 3.x) puis installez en suivant [ces instructions](http://maven.apache.org/install.html).
   * Sous Linux : Lancez la commande :

```bash
    sudo apt-get install maven
```

### Installer un IDE

Pour développer en Java, vous pouvez utiliser l'IDE de votre choix, par exemple :

- Sublime Text (non libre) : https://www.sublimetext.com/
- Autre possibilité : [Idea](https://www.jetbrains.com/idea/download/) (non libre mais fonctionnement très avancé).

## Niveau III : maîtriser les commandes usuelles

Ce troisième niveau permet de découvrir les quelques commandes que vous utiliserez tout le temps si vous développez sur Cesium+ Pod. Vous y apprendrez : 

- à configurer le projet, notamment les paramètres réseau (du noeud ES, du noeud Duniter, etc.);

- à compiler le projet;

- à lancer votre noeud ElasticSearch avec les plugins Cesium+ Pod;

### Configurer le projet

La configuration utilisée pour le développement est visible dans le fichier : `/cesium-plus-pod-assembly/src/test/es-home/config/elasticsearch.yml`

#### Configuration du noeud Duniter

Si vous avez un noeud Duniter qui est lancé localement, configurez le en modifiant les propriétés suivantes :
 
```yml
#
# Duniter node to synchronize
#
duniter.host: localhost
duniter.port: 10901    <- à remplacer par le port de votre noeud
```

Si vous n'avez pas de noeud local, conservez la configuration par défaut :

```yml
#
# Duniter node to synchronize
#
duniter.host: g1.duniter.org
duniter.port: 10901
```

#### Désactivation de la couche de sécurité

Cesium+ pod a une couche de sécurité, qui empêche l'appel à des URL non autorisées. 
Nous allons **désactiver cette couche de sécurité** pour faciliter l'apprentissage qui va suivre.

Modifiez comme suit, modifier le fichier de configuration `cesium-plus-pod-assembly/src/main/assembly/config/elasticsearch.yml` :

```bash
duniter.security.enable: false
```

### Compiler le projet

La compilation du projet utilise Maven, par la commande  `mvn` (à ne pas confondre avec `nvm`).

#### Tout compiler

Executez la commande suivante pour compiler l'ensemble du projet :

```bash
mvn install
```

Si tout c'est bien passé, vous devriez obtenir quelque chose qui ressemble à cela :

```bash
(...)
[INFO] Building zip: /home/user/git/cesium-plus-pod/cesium-plus-pod-assembly/target/cesium-plus-pod-0.3.5-SNAPSHOT-standalone.zip
[INFO] 
[INFO] --- maven-install-plugin:2.4:install (default-install) @ cesium-plus-pod-assembly ---
[INFO] Installing /home/user/git/cesium-plus-pod/cesium-plus-pod-assembly/pom.xml to /home/eis/.m2/repository/org/duniter/cesium-plus-pod-assembly/0.3.5-SNAPSHOT/cesium-plus-pod-assembly-0.3.5-SNAPSHOT.pom
[INFO] Installing /home/user/git/cesium-plus-pod/cesium-plus-pod-assembly/target/cesium-plus-pod-0.3.5-SNAPSHOT-standalone.zip to /home/eis/.m2/repository/org/duniter/cesium-plus-pod-assembly/0.3.5-SNAPSHOT/cesium-plus-pod-assembly-0.3.5-SNAPSHOT-standalone.zip
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] Cesium+ pod :: Core plugin ........................ SUCCESS [8.954s]
[INFO] Cesium+ pod :: User plugin ........................ SUCCESS [1.039s]
[INFO] Cesium+ pod :: Subscription plugin ................ SUCCESS [0.804s]
[INFO] Cesium+ pod :: ElasticSearch Assembly ............. SUCCESS [4.747s]

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 48.733 s
[INFO] Finished at: 2016-11-17T11:06:16+01:00
[INFO] Final Memory: 74M/741M
[INFO] ------------------------------------------------------------------------
```

Bravo, vous avez compilé le projet avec succès !


#### Compiler sans les tests unitaires

Par la suite, vous pourrez **ignorer les tests unitaires**, de cette manière : 

```bash
mvn install -DskipTests
```

Cela permet une compilation plus rapide.
 
### Lancer un noeud ElasticSearch

Il ne vous reste plus qu'à lancer votre pod local.

Lancez la commande suivante : 

```bash
mvn install -Prun -pl cesium-plus-pod-assembly
```

Vous devriez avoir maintenant :

```bash
[2016-11-17 13:29:35,874][INFO ][transport                ] [Att-Lass] publish_address {127.0.0.1:9300}, bound_addresses {[::1]:9300}, {127.0.0.1:9300}
[2016-11-17 13:29:35,882][INFO ][discovery                ] [Att-Lass] cesium-plus-pod-g1-TEST/HjDNQrTTSuyfmzGEbY7qNQ
[2016-11-17 13:29:38,956][INFO ][cluster.service          ] [Att-Lass] new_master {Att-Lass}{HjDNQrTTSuyfmzGEbY7qNQ}{127.0.0.1}{127.0.0.1:9300}, reason: zen-disco-join(elected_as_master, [0] joins received)
[2016-11-17 13:29:38,990][INFO ][http                     ] [Att-Lass] publish_address {127.0.0.1:9200}, bound_addresses {[::1]:9200}, {127.0.0.1:9200}
[2016-11-17 13:29:38,991][INFO ][node                     ] [Att-Lass] started
[2016-11-17 13:29:39,515][INFO ][gateway                  ] [Att-Lass] recovered [6] indices into cluster_state
[2016-11-17 13:29:41,655][INFO ][cluster.routing.allocation] [Att-Lass] Cluster health status changed from [RED] to [YELLOW] (reason: [shards started [[registry][1], [registry][1]] ...]).
[2016-11-17 13:29:45,756][INFO ][node                     ] Checking Duniter indices...
[2016-11-17 13:29:45,766][INFO ][node                     ] Checking Duniter indices... [OK]
[2016-11-17 13:29:58,052][INFO ][duniter.blockchain       ] [g1-test] [cgeek.fr:9330] Indexing last blocks...
```

### Vérifier le fonctionnement

Pour vérifier le bon fonctionnement de votre noeud ES, ouvrez un navigateur à l'adresse suivante : http://127.0.0.1:9200  (il s'agit de l'adresse par défaut d'ElasticSearch).

Vous devriez voir le contenu suivant : 
<img src="https://forum.duniter.org/uploads/default/original/2X/f/fb3d42bb463334b4223d40c1a45a16caa8f589a2.png" width="506" height="260"/>

Bravo, votre pod Cesium+ est fonctionnel !

### En cas de problème au lancement

#### Erreur `BindTransportException[Failed to bind to [9300-9400]];`

Cette erreur indique qu'ElasticSearch n'a pas pu démarrer sur le port demandé.

Vous pouvez changer de port (ou l'IP), en éditant les propriétés suivantes du fichier `/cesium-plus-pod-assembly/src/test/es-home/config/elasticsearch.yml` : 

```bash
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 192.168.233.1    <-- Remplacez par votre IP
#
# Set a custom port for HTTP:
#
http.port: 9200   <-- Remplacez par un port libre de votre machine (plage 9200-9300 par défaut)
```

## Niveau IV : Se repérer dans le code 

### Répérer les couches logicielles

Ouvrir votre IDE, et ouvrir le projet Duniter4j.

Dans le répertoire `cesium-plus-pod-core/src/main/java`, cherchez et répérez dans le code : 

- les controlleurs REST : package `org.duniter.elasticsearch.rest`

- les services d'indexation : package `org.duniter.elasticsearch.service`.

  * Il existe un service d'indexation par type de stockage. Par exemple : `BlockchainService`, `UserService`, etc.

### Aller plus loin dans le code

Cesium+ Pod s'appuie sur ElasticSearch **en version 2.4**. D'excellentes documentations sont présentes sur le web : https://www.elastic.co/guide/en/elasticsearch/reference/2.3/index.html.

## Niveau V : Requêtage sur ES API

Nous allons requeter l'indexation de la BlockChain `g1-test`, qui s'est fait dès le démarrage de votre noeud ElastiSearch. Nous appellons cette indexation l'**ES API**. 

Il existe plusieurs manière de requéter un noeud ES : 

- Requêtes HTTP GET

- Requêtes HTTP POST

### Requêtes GET

En utilisant un navigateur, vous allez requêter .

- [GET-1] Visualisez un bloc quelconque (par exemple le premier #0): http://localhost:9200/g1-test/block/0
  
Etudiez ensuite le format du résultat : 

```json
{
  "_index":"g1-test",
  "_type":"block",
  "_id":"0",
  "_version":1,
  "found":true,
  "_source": {
    "number": 0,
    "...": "..."
  }
}
```

> Observez qu'ElasticSeach a ajouté des informations : `_index`, `_type`, etc.

- [GET-2] Pour éviter d'avoir les informations additionnelles, ajoutez `/_source` : http://localhost:9200/g1-test/block/0/_source

> Notez que le bloc courant est accessible en `/g1-test/block/current`
        
- [GET-3] Récupérer **uniquement** les champs `hash`, `dividend` et `memberCount`, pour le bloc #125 : http://localhost:9200/g1-test/block/125/_source?_source=number,hash,dividend,membersCount

> Notez que vous pouvez avoir une meilleure présentation en ajoutant "`&pretty`" dans l'adresse;

- [GET-4] Les blocks qui référence une clef publique (recherche full text) : http://localhost:9200/g1-test/block/_search?q=8Fi1VSTbjkXguwThF4v2ZxC5whK7pwG2vcGTkPUPjPGU

> Vous pouvez rechercher sur n'importe quelle chaine (recherche `full-text`), via cet option "`q=`" 

### Requêtes via HTTP POST

Les requetes de type POST permettent des filtrage beaucoup plus fins. C'est généralement ce type de requêtes qui est utilisées dans des logiciels clients. On peut par exemple ajouter une mise en surbrillance; requêter plusieurs index en une seule fois, etc.

Nous utiliserons `curl` pour requeter notre noeud ElasticSearch.
Si vous ne l'avez pas encore installé, executer simplement (sous Linux) :

```bash
sudo apt-get install curl
```

Dans un terminal, exécuter les commandes suivantes :

- [POST-1] Récupérez les blocs ayant un dividende universel, en sélectionnant **quelques champs** uniquement (dividend, number, hash). : 

```bash
curl -XGET 'http://localhost:9200/g1-test/block/_search?pretty' -d '{
"query": {
        "filtered" : {
            "filter": {
                "exists" : { "field" : "dividend" }
            }
        }
    },
    "_source": ["number", "dividend", "hash", "membersCount"],
   size: 10000
 }'
```
        
- [POST-2] Récupérez tous les blocs de #0 à #100 :

```bash
curl -XGET 'http://localhost:9200/g1-test/block/_search' -d '{
    "query": {
       "bool": {
           "should": {
                "range":{
                    "number":{
                        "lte":100
                    }
                }
            }
       }
    }
}'
```

### Toute la doc pour aller plus loin dans les requetes

Voici la documentation pour aller plus loin :

- ElasticSearch [official web site](http://www.elastic.co/guide/en/elasticsearch/reference/1.3/docs-get.html#get-source-filtering)
- un bon [tutoriel](http://okfnlabs.org/blog/2013/07/01/elasticsearch-query-tutorial.html) 


## Niveau VI : Requêtage sur Cesium+ API

Duniter4j permet aussi de stocker et d'indexer les données hors BlockChain, comme celles utilisées par Cesium+ : 

- `/user/profile` : les profiles utilisateurs (nom complet, réseaux sociaux, avatar, etc.)   
- `/message/inbox` : les messages privées recus    
- `/message/outbox` : les messages privées envoyés    
- `/page/record` : les pages de l'[annuaire des pages Cesium+](https://g1.duniter.fr/#/app/wot/page/lg);   
- `/page/comment` : les commentaires sur les annonces
- `/subscription/record` : les abonnements aux services en ligne (par exemple les notifications par email)

> La document de l'API HTTP est disponible [ici](./REST_API.html).

### Requêtes sur `g1-test.data.duniter.fr`

Nous allons requêter le noeud `g1-test.data.duniter.fr` déployé sur la monnaie `Ğ1-test`.

> **Note** : Ce noeud est configuré AVEC la couche de sécurité Duniter4j. Les accès sur des URL non autorisés renverront une page vide (erreur HTTP 404).  


#### Requêtes GET

- [GET-5] Liste des pages avec le mot `boulangerie` : [/page/record/_search?pretty&q=boulangerie](https://g1-test.data.duniter.fr/page/record/_search?pretty&amp;q=boulangerie)

- [GET-5] Liste des messages à destination de la clef `5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of` : [/message/inbox/_search?pretty&receiver=38MEAS...](https://g1-test.data.duniter.fr/message/inbox/_search?pretty&amp;receiver=5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of)

> Observez que le contenu des messages est chiffré. Seul le destinataire (`recipient`) peut y accéder.


#### Requêtes POST


- [POST-3] Liste des [pages soumises par kimamila](https://g1-test.duniter.fr/#/app/wot/5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of/kimamila) (pubkey=`5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of`)

```bash
curl -XPOST "https://g1-test.data.duniter.fr/page/record/_search?pretty" -d'
{
  "query": {        
        "bool":{
            "filter": [
                {"term":{
                        "issuer":"5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of"
                    }
                }                
            ]
        }
  },
  "_source": ["title", "description", "time"],
  "from":0,
  "size":100
}'
```

Normalement, vous devriez récupérer **au moins une page**.

> **Note** : Dans Cesium+, ce même contenu est [visible ici](http://g1-test.duniter.fr/#/app/page/view/AV9fErJAW0KLAFOJm34y/boulangerie-chez-marcel-test)   
> Notez que l'option dans cette adresse utilise `q=` pour la recherche `full text`.

Copiez la valeur du champ `_id`, et utilisez la dans la prochaine requête :

- [POST-4] : Récupérez les commentaires [de la page](http://g1-test.duniter.fr/#/app/page/view/AV9fErJAW0KLAFOJm34y/boulangerie-chez-marcel-test) : 

```bash
curl -XPOST "https://g1.data.duniter.fr/page/comment/_search?pretty" -d'
{
  "query": {
        "bool":{
            "filter": [
                {"term":{
                        "record": "  ...   "   <-- mettre ICI la valeur [_id] de la page
                    }
                }
            ]
        }
  }
}'
```

