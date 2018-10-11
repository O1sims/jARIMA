
initiateArimaWorkspace <- function() {
  library(forecast)
  library(magrittr)
  
  "### Classifier worker ready on port : " %>%
    paste0(6311) %>%
    print()
}
