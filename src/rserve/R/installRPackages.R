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
if (!("devtools" %in% installed.packages())) {
  utils::install.packages(
    "devtools",
    repos = cranRepo)
}

# CRAN packges to install with versions
packageList <- list(
  "Rserve" = "1.7-3",
  "RSclient" = "0.7-3",
  "forecast" = "8.3")

# Generate an array of packages that have not already been installed
newPackages <- subset(
  x = names(packageList),
  subset = !(names(packageList) %in% installed.packages()))

# Install any new packages from CRAN
if (length(newPackages) > 0) {
  for (package in newPackages) {
    devtools::install_version(
      package = package,
      version = packageList[[package]],
      repos = cranRepo)
  }
}
