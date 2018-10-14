#' Install necessary R packages
#'
#' @title Install R Packages
#'
#' @description This script installs all necessary packages that have not
#'  already been installed into the `RServe` Docker container.
#'
#' @details The installation process is as follows:
#'  (1) Check whether each package has ready been installed on the system; and
#'  (2) Install any missing packages securly from CRAN.
#'
#' Note that this file is (and must be) a script, not a function.


# CRAN mirror
cranRepo <- 'http://cran.us.r-project.org'

# Install devtools if not installed already
# Required for installing versions
packageList <- c("Rserve", "RSclient", "forecast")
for (package in packageList) {
  if (!(package %in% installed.packages())) {
    utils::install.packages(
      package,
      repos = cranRepo)
  }
}
