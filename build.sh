#!/bin/sh -e
# Build the components of the ARIMA application

case $1 in
    help)
        cat <<EOF

EOF
        ;;
    up)
        service mongod stop
        cd src
        docker-compose up
        ;;
    down|stop)
        cd src
        docker-compose $1
        ;;
    all)
        service mongod start
        cd src/api/
        mvn package
        docker build -t arima:latest .
        ;;
esac
