
performArima <- function(tsData) {
  fut <- tsData %>%
    auto.arima() %>%
    forecast(1) %>%
    as.data.frame()
  return(c(
    fut$`Hi 95` %>% as.double(),
    fut$`Lo 95` %>% as.double()))
}