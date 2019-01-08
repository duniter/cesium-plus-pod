#!/bin/bash

CURRENCY=g1

# Get PID
PID=`ps -efl | grep cesium-plus-pod | grep ${CURRENCY}/lib | awk '/^([0-9]+) ([^\s]) ([a-zA-Z0-9]+) ([0-9]+).*/ {printf "%s", $4}'`

if [[ "$PID" != "" ]];
then
        echo "Error: Cesium+ pod already started!"
        exit -1
else

        # Limit JVM heap size
        ES_JAVA_OPTS="$ES_JAVA_OPTS -Xmx2g"
        export ES_JAVA_OPTS

        cd /opt/cesium-plus-pod-${CURRENCY}/bin
        ./elasticsearch -d
        echo "Cesium+ pod started on ${CURRENCY} currency !"
        echo "...to follow log: tail -f /opt/cesium-plus-pod-${CURRENCY}/logs/${CURRENCY}-es-data.log"
fi

