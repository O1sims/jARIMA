library(httr)
library(jsonlite)
library(magrittr)



compareARIMA <- function(dataPoints, minRange, maxRange, iterations) {
  # Instantiate result vectors
  jArima <- rArima <- c()
  
  for (i in 1:iterations) {
    # Generate a list or random numbers
    tsData <- as.numeric(AirPassengers)
    
    # Convert tsData to JSON
    postJSON <- list(
      forecastPeriod = 10,
      tsdata = tsData)
    
    # Get forecast from JARIMA
    JARIMAResult <- getARIMAForecast(
      model = 'j-arima', 
      payloadData = postJSON)
    
    jArima %<>% 
      append(JARIMAResult)
    
    # Get forecast from RARIMA
    RARIMAResult <- getARIMAForecast(
      model = 'r-arima', 
      payloadData = postJSON)
    
    rArima %<>% 
      append(RARIMAResult)
  }
  
  # Create list of summary statistics
  results.list <- list(
    dataPoints = dataPoints,
    iterations = iterations,
    jArimaResults = jArima,
    rARIMAResults = rArima,
    percentageDiff = abs(((rArima - jArima)/rArima) * 100),
    variance = var(jArima, rArima),
    correlation = cor(jArima, rArima))
  
  # Return summary statistics
  return(results.list)
}


getARIMAForecast <- function(model = c("j-arima", "r-arima"), payloadData) {
  # Begin stopclock
  t <- Sys.time()
  
  # Send JSON to ARIMA endpoint in Java app
  RequestResponse <- 'http://localhost:9000/api/' %>%
    paste0(model, '/') %>%
    httr::POST(
      body = payloadData, 
      encode = "json")
  
  "Time taken for " %>%
  paste0(model, " : ", Sys.time() - t) %>%
    print()
  
  # Unpack ARIMA results
  ARIMAResults <- RequestResponse %>% 
    httr::content()
  
  # Return first forecast result
  return(ARIMAResults$forecast)
}


avgPercentageDiff <- variance <- correlation <- dataPoints <- c()
for (i in seq(50, 1000, 50)) {
  print(i)
  analysisResults <- compareARIMA(
    dataPoints = i, 
    iterations = 5, 
    minRange = 100, 
    maxRange = 200)
  variance %<>% 
    append(analysisResults$variance[[1]])
  avgPercentageDiff %<>% 
    append(mean(analysisResults$percentageDiff))
  dataPoints %<>% 
    append(i)
  correlation %<>%
    append(analysisResults$correlation)
}

