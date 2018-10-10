FROM r-base

EXPOSE 6311
VOLUME '/var/lib/arima'

ADD R /opt/arima/R
RUN Rscript /opt/arima/R/installRPackages.R

ADD bin /opt/arima/bin
WORKDIR /var/lib/arima/
CMD /opt/arima/bin/run.sh
