#!/bin/bash

# Get ES PID
PID=`ps -efl | grep cesium-plus-pod | grep lib | awk '{printf "%s", $4}'`

if [ "$PID" != "" ];
then
        echo "Stopping ES node running on PID $PID..."
        sudo kill -15 $PID

        sleep 5s

        # Check if still alive
        PID=`ps -efl | grep cesium-plus-pod | grep g1/lib | awk '{printf "%s", $4}'`
        if [ "$PID" != "" ];
        then
                sleep 10s
        fi

        PID=`ps -efl | grep cesium-plus-pod | grep g1/lib | awk '{printf "%s", $4}'`
        if [ "$PID" != "" ];
        then 
                echo "Error: Unable to stop ES node !"
                exit -1
        else
                echo "ES node stopped"
        fi

else
        echo "ES node not running!"
fi

