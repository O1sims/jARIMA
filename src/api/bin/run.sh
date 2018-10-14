#!/bin/sh -e

if [ $# -eq 0 ]; then
    echo "Specify service to run!"
    exit 1
fi

SERVICE_NAME=$1
CONFIG_FILE=${2:-"config/config.yml"}
cd /opt/cyberlytic/

if [ $SERVICE_NAME = "api" ]; then
    echo "Running 'initial_setup'..."
    java -cp lib/cyberlytic-api.jar com.cyberlytic.api.Run initial_setup $CONFIG_FILE
    echo "Running 'server'..."
    java -cp lib/cyberlytic-api.jar com.cyberlytic.api.Run server $CONFIG_FILE

else
    echo "Running '$SERVICE_NAME'..."
    java -cp lib/cyberlytic-api.jar com.cyberlytic.services.Run $SERVICE_NAME $CONFIG_FILE
fi


