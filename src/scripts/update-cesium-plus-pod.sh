#!/bin/bash

VERSION=$1
OLD_VERSION=$2

if [ "${VERSION}" == "" ]; then
        echo "ERROR: Missing version argument !"
        echo " "
        echo "usage: sudo ./update-es.sh <version> [<old_version>]"
        exit
fi
if [ "${OLD_VERSION}" == "" ]; then
        OLD_VERSION=`ps -efl | grep cesium-plus-pod | grep g1/lib | sed -r 's/.*cesium-plus-pod-([0-9.]+)-g1.*/\1/g'`
        if [ "${OLD_VERSION}" == "" ]; then
                echo "Error: unable to known previous version"
                exit
        fi
fi

READLINK=`which readlink`
if [ -z "$READLINK"  ]; then
  message "Required tool 'readlink' is missing. Please install before launch \"$0\" file."
  exit 1
fi

# ------------------------------------------------------------------
# Ensure BASEDIR points to the directory where the soft is installed.
# ------------------------------------------------------------------
SCRIPT_LOCATION=$0
if [ -x "$READLINK" ]; then
  while [ -L "$SCRIPT_LOCATION" ]; do
    SCRIPT_LOCATION=`"$READLINK" -e "$SCRIPT_LOCATION"`
  done
fi 

export BASEDIR=`dirname "$SCRIPT_LOCATION"`                                                                                                                                                                                                        
cd $BASEDIR 

echo "--- Downloading cesium-plus-pod-standalone v$VERSION... ----------------------"

if [ -f "downloads/cesium-plus-pod-${VERSION}-standalone.zip" ]; then
        echo "...removing file, as it already exists in ./downloads/cesium-plus-pod-${VERSION}-standalone.zip"
        rm ./downloads/cesium-plus-pod-${VERSION}-standalone.zip
fi

if [ ! -e "downloads" ]; then
        mkdir downloads
fi

cd downloads
wget -kL https://github.com/duniter/duniter4j/releases/download/cesium-plus-pod-${VERSION}/cesium-plus-pod-${VERSION}-standalone.zip
cd ..

if [ -f "downloads/cesium-plus-pod-${VERSION}-standalone.zip" ]; then
        echo ""
else
        echo "Error: unable to dowlonad this version!"
        exit -1
fi

echo "--- Installating cesium-plus-pod v$VERSION... ---------------------"
if [ -d "/opt/cesium-plus-pod-${VERSION}-g1" ]; then
        echo "Error: Already installed in /opt/cesium-plus-pod-${VERSION}-g1 !"
        exit -1
fi

unzip -o ./downloads/cesium-plus-pod-${VERSION}-standalone.zip
mv cesium-plus-pod-${VERSION} cesium-plus-pod-${VERSION}-g1
sudo mv cesium-plus-pod-${VERSION}-g1 /opt/
sudo rm /opt/cesium-plus-pod-g1
sudo ln -s /opt/cesium-plus-pod-${VERSION}-g1 /opt/cesium-plus-pod-g1

mkdir /opt/cesium-plus-pod-${VERSION}-g1/data
mv /opt/cesium-plus-pod-${VERSION}-g1/config/elasticsearch.yml /opt/cesium-plus-pod-${VERSION}-g1/config/elasticsearch.yml.ori

stop-cesium-plus-pod.sh

if [ "$OLD_VERSION" != "$VERSION" ];
then
        echo "--- Restoring files (data+config) from previous version $OLD_VERSION... ---------------------"
        tar -cvf /opt/cesium-plus-pod-${OLD_VERSION}-g1/data/save.tar.gz /opt/cesium-plus-pod-${OLD_VERSION}-g1/data/g1-*
        mv /opt/cesium-plus-pod-${OLD_VERSION}-g1/data/g1-* /opt/cesium-plus-pod-${VERSION}-g1/data  
        cp /opt/cesium-plus-pod-${OLD_VERSION}-g1/config/elasticsearch.yml /opt/cesium-plus-pod-${VERSION}-g1/config
fi

#./start-es-nodes.sh

echo "--- Successfully installed cesium-plus-pod v$VERSION ! -------------"
echo ""

