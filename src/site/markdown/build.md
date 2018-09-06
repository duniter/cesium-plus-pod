
# Build from source

## Get source and compile

### Install Java JDK

- Linux (Debian, Ubuntu): 

```bash
sudo apt-get install openjdk-8-jdk
```

- Windows: download then unzip  JDK (8 ou +) from [Oracle web site](http://oracle.com/java/index.html)


### Install LibSodium

 
- Linux: follow [this instructions](https://download.libsodium.org/doc/installation/index.html) (anglais).

   * After install, check that the file `libsodium.so` exists inside directory `/usr/local/lib` or `/opt/local/lib`.

   * If not exists, but at another location, please create a symbolic link under directory `/usr/local/lib` or `/opt/local/lib`.

- Windows: Nothing to do (the lib `sodium.dll` is already include in the included library `duniter4j`);


### Installer Apache Maven 3

Install [Apache Maven 3](http://maven.apache.org):

- Linux (Debian, Ubuntu): 

```bash
    sudo apt-get install maven
```

- Windows : [download here](http://maven.apache.org/download.cgi) (version 3.x) then install using [this instructions](http://maven.apache.org/install.html).


- Install [Maven 3](http://maven.apache.org/): `sudo apt-get install maven`

### Get source the compile

- Get the source code, then compile using Maven:

```
git clone git@git.duniter.org:clients/cesium-grp/cesium-plus-pod.git
cd cesium-plus-pod
git submodule init
git submodule sync
git submodule update

mvn install -DskipTests
```
 
- Then, package all binaries:

```bash
mvn install -DskipTests -DperformRelease
```

## Deploy binaries

You will need to have access to project site repository. 

To deploy binaries:

```bash
mvn release:prepare
mvn release:perform
```

## Deploy the web site

To deploy the web site:

```bash
mvn site-deploy -DperformRelease
```
