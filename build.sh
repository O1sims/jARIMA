#!/bin/sh -e
# Build the components of the ARIMA application

case $1 in
    help)
        cat <<EOF

EOF
        ;;
    up)
        cd src
        docker-compose up
        ;;
    down|stop)
        cd src
        docker-compose $1
        ;;
    all)
        cd src/rserve/
        docker build -t rserve:latest .
        cd ../api/
        mvn package
        docker build -t arima:latest .
        ;;
esac
