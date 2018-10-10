#!/bin/sh

if [ $# -eq 0 ]; then
    echo "Using default..."
    # exec Rscript /opt/cyberlytic/profileR/setupClassifierEnvironment.R
    exec Rscript -e "Rserve::Rserve(port = 6311L, args = c(\"--RS-enable-remote\", \"--no-save\")); Sys.sleep(5); RSclient::RS.eval(rsc = RSclient::RS.connect(port = 6311L), x = { print(\"test\") }, wait = FALSE)" &

else
    DAEMON_NAME=$1
fi

sleep 10
echo "Done!..."
tail -f /var/lib/arima/logs/*.log
