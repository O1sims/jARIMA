
initiateArimaWorkspace <- function() {
  library(forecast)
  library(magrittr)
  
  "### Classifier worker ready on port : " %>%
    paste0(6311) %>%
    print()
}


performArima <- function(tsData) {
  fut <- tsData %>%
    auto.arima() %>%
    forecast(1) %>%
    as.data.frame()
  return(c(
    fut$`Hi 95` %>% as.double(),
    fut$`Lo 95` %>% as.double()))
}