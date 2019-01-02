#!/bin/bash

CURRENCY=g1

# Get ES PID
PID=`ps -efl | grep cesium-plus-pod | grep ${CURRENCY}/lib | awk '{printf "%s", $4}'`

if [[ "$PID" != "" ]];
then
        echo "Stopping Cesium+ Pod running on PID $PID..."
        sudo kill -15 $PID

        sleep 5s

        # Check if still alive
        PID=`ps -efl | grep cesium-plus-pod | grep ${CURRENCY}/lib | awk '{printf "%s", $4}'`
        if [[ "$PID" != "" ]];
        then
                sleep 10s
        fi

        PID=`ps -efl | grep cesium-plus-pod | grep ${CURRENCY}/lib | awk '{printf "%s", $4}'`
        if [[ "$PID" != "" ]];
        then 
                echo "Error: Unable to stop Cesium+ Pod !"
                exit -1
        else
                echo "Cesium+ Pod stopped"
        fi

else
        echo "Cesium+ Pod not running!"
fi

