#!/bin/bash

VERSION=$1
OLD_VERSION=$2
ASSET_BASENAME=cesium-plus-pod
ASSET=${ASSET_BASENAME}-${VERSION}-standalone
CURRENCY=g1
REPO=duniter/cesium-plus-pod
ASSET_ZIP_URL=https://github.com/${REPO}/releases/download/v${VERSION}/${ASSET}.zip

if [[ "${VERSION}" == "" ]]; then
        echo "ERROR: Missing version argument !"
        echo " "
        echo "usage: sudo ./update-es.sh <version> [<old_version>]"
        exit
fi
if [[ "${OLD_VERSION}" == "" ]]; then
        OLD_VERSION=`ps -efl | grep ${ASSET_BASENAME} | grep ${CURRENCY}/lib | sed -r "s/.*${ASSET_BASENAME}-([0-9.]+)-${CURRENCY}.*/\1/g"`
        if [[ "${OLD_VERSION}" == "" ]]; then
                echo "Error: unable to known previous version"
                exit
        fi
fi

READLINK=`which readlink`
if [[ -z "$READLINK"  ]]; then
  message "Required tool 'readlink' is missing. Please install before launch \"$0\" file."
  exit 1
fi

# ------------------------------------------------------------------
# Ensure BASEDIR points to the directory where the soft is installed.
# ------------------------------------------------------------------
SCRIPT_LOCATION=$0
if [[ -x "$READLINK" ]]; then
  while [[ -L "$SCRIPT_LOCATION" ]]; do
    SCRIPT_LOCATION=`"$READLINK" -e "$SCRIPT_LOCATION"`
  done
fi 

export BASEDIR=`dirname "$SCRIPT_LOCATION"`                                                                                                                                                                                                        
cd $BASEDIR 

echo "--- Downloading ${ASSET} ... ----------------------"

if [[ ! -e "downloads" ]]; then
        mkdir downloads
fi

if [[ -f "./downloads/${ASSET}.zip" ]]; then
        echo "Already downloaded. Skipping"
else
    cd downloads
    wget -kL ${ASSET_ZIP_URL}
    cd ..
fi


if [[ -f "downloads/${ASSET}.zip" ]]; then
        echo ""
else
        echo "Error: unable to download this version!"
        exit -1
fi

echo "--- Installing cesium-plus-pod v$VERSION... ---------------------"
if [[ -d "/opt/cesium-plus-pod-${VERSION}-${CURRENCY}" ]]; then
        echo "Error: Already installed in /opt/cesium-plus-pod-${VERSION}-${CURRENCY} !"
        exit -1
fi

unzip -o ./downloads/${ASSET}.zip
mv ${ASSET_BASENAME}-${VERSION} ${ASSET_BASENAME}-${VERSION}-${CURRENCY}
sudo mv ${ASSET_BASENAME}-${VERSION}-${CURRENCY} /opt/
sudo rm /opt/${ASSET_BASENAME}-${CURRENCY}
sudo ln -s /opt/${ASSET_BASENAME}-${VERSION}-${CURRENCY} /opt/${ASSET_BASENAME}-${CURRENCY}

mkdir /opt/${ASSET_BASENAME}-${VERSION}-${CURRENCY}/data
mv /opt/${ASSET_BASENAME}-${VERSION}-${CURRENCY}/config/elasticsearch.yml /opt/${ASSET_BASENAME}-${VERSION}-${CURRENCY}/config/elasticsearch.yml.ori

stop-cesium-plus-pod.sh

if [[ "$OLD_VERSION" != "$VERSION" ]];
then
        echo "--- Restoring files (data+config) from previous version $OLD_VERSION... ---------------------"
        tar -cvf /opt/${ASSET_BASENAME}-${OLD_VERSION}-${CURRENCY}/data/save.tar.gz /opt/${ASSET_BASENAME}-${OLD_VERSION}-${CURRENCY}/data/${CURRENCY}-*
        mv /opt/${ASSET_BASENAME}-${OLD_VERSION}-${CURRENCY}/data/${CURRENCY}-* /opt/${ASSET_BASENAME}-${VERSION}-${CURRENCY}/data
        cp /opt/${ASSET_BASENAME}-${OLD_VERSION}-${CURRENCY}/config/elasticsearch.yml /opt/${ASSET_BASENAME}-${VERSION}-${CURRENCY}/config
fi

#./start-es-nodes.sh

echo "--- Successfully installed ${ASSET_BASENAME} v$VERSION ! -------------"
echo ""

