#!/bin/bash


PID=`ps -efl | grep cesium-plus-pod | grep g1/lib | awk '/^([0-9]+) ([^\s]) ([a-zA-Z0-9]+) ([0-9]+).*/ {printf "%s", $4}'`

if [ "$PID" != "" ];
then
        echo "Error: Cesium+ pod already started!"
        exit -1
else
        cd /opt/cesium-plus-pod-g1/bin
        ./elasticsearch -d
        echo "Cesium+ pod started !"
        echo "...to follow log: tail -f /opt/cesium-plus-pod-g1/logs/g1-es-data.log"
fi

