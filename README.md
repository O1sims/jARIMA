# ARIMA

## Overview

This a Java-based web application that develops a allows a user to run .

## Technology stack

The Java web application uses [Springboot](https://www.djangoproject.com/) as the backend framework, [R]() as the comparable analytics engine with the daemonised [Rserve]() client to process time series data and [Angular](https://angular.io/) as the frontend framework. Documentation of the RESTful API service is handled by [Swagger](https://swagger.io/). Development is done within a [Docker](https://www.docker.com/).

## Building the application

We use Docker in development to make it easy to build, run and share. We have three Dockerfile's: one the builds the Java backend, one that sets up the Rserve client and one that builds the Angular GUI. All components of the application can be built from the `build.sh` file:
```
git clone https://github.com/O1sims/ARIMA.git
cd ARIMA
bash build.sh all
```
The `build.sh` file has multiple options to choose from.

## Running the application

The `build.sh` file also coordinates the running of the application. Specifically, the application can be run with the following command:
```
bash build.sh up
```
Likewise, the application can be brought down with `bash build.sh down`.

By default, the main ARIMA application is accessible on port `3000`, the API and backend components are on port `9000`, and the Rserve client is run on port `6311`. The `docker-compose.yml` can be altered to allow these services to run on different ports. Swagger API documentation can be found at `localhost:9000/api/swagger-ui.html`.

Environmental variables for the application can be changed in the `docker-compose.yml` file.

## Using the application

There are two POST API endpoints are developed for this application: one that runs ARIMA analysis in R (`.../api/r-arima/`) and one that runs ARIMA analysis in Java (`.../api/j-arima/`).

### Request

Both API endpoints consume the same payload structure consisting of:
[] "forecastPeriod". A positive integer; and
[] "tsData". An array of doubles
Consider an example structure below:
```
{
  "forecastPeriod": 2,
  "tsData": [
    1,2,3,4,5,6,7,8,9,10,11.12
  ]
}
```

### Response

Overall, the ARIMA API's will respond with similar outputs.

The R ARIMA endpoint produces:
```
{
  "forecast": [
    13, 14
  ],
  "lowerBound": [
    12.5, 13.5
  ],
  "upperBound": [
    13.5, 14.5
  ]
}
```

The Java ARIMA endpoint produces extra data, which relate to the models goodness of fit:
```
{
  "aic": 0,
  "forecast": [
    0
  ],
  "lowerBound": [
    0
  ],
  "maxNormalizedVariance": 0,
  "rmse": 0,
  "upperBound": [
    0
  ]
}
```
