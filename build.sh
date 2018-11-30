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
        docker build -t arima-gui:latest src/webserver/.
        docker build -t rserve:latest src/rserve/.
        cd src/api/
        mvn package
        docker build -t arima:latest .
        ;;
esac
