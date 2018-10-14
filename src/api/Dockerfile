FROM openjdk:8-jdk-alpine

VOLUME /tmp

RUN mkdir /arima
WORKDIR /arima
ADD target/api-0.0.1.jar /arima

EXPOSE 9000
