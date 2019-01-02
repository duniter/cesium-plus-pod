#!/bin/bash

CURRENCY=g1

PID=`ps -efl | grep cesium-plus-pod | grep ${CURRENCY}/lib | awk '/^([0-9]+) ([^\s]) ([a-zA-Z0-9]+) ([0-9]+).*/ {printf "%s", $4}'`

if [[ "$PID" != "" ]];
then
        echo "Error: Cesium+ pod already started!"
        exit -1
else
        cd /opt/cesium-plus-pod-${CURRENCY}/bin
        ./elasticsearch -d
        echo "Cesium+ pod started on ${CURRENCY} currency !"
        echo "...to follow log: tail -f /opt/cesium-plus-pod-${CURRENCY}/logs/g1-es-data.log"
fi

