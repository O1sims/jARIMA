library(httr)
library(forecast)
library(jsonlite)
library(magrittr)



compareARIMA <- function(dataPoints, minRange, maxRange, forecastPeriod) {
  # Generate a list or random numbers
  tsData <- dataPoints %>% 
    runif(
      min = minRange,
      max = maxRange)
  
  # Pipe this into the ARIMA forecast mechanism
  arimaAnalysis <- tsData %>%
    auto.arima() %>%
    forecast(forecastPeriod) %>%
    data.frame()
  
  # Convert tsData to JSON
  postJSON <- list(
    forecastPeriod = forecastPeriod,
    tsdata = tsData)
  
  # Send JSON to JARIMA endpoint in Java app
  requestResponse <- 'http://localhost:9000/api/j-arima/' %>%
    httr::POST(
      body = postJSON, 
      encode = "json")
  
  # Unpack JARIMA results
  JARIMAResults <- requestResponse %>% 
    httr::content()
  
  
}
