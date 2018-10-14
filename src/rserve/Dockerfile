FROM r-base

RUN apt-get update
RUN apt-get install libcurl4-openssl-dev

RUN mkdir -p /opt/arima/R
ADD R /opt/arima/R
RUN Rscript /opt/arima/R/installRPackages.R

ADD Rserv.conf /Rserv.conf

EXPOSE 6311
