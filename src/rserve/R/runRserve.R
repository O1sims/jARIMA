require(Rserve)

Rserve::run.Rserve(
  debug = FALSE,
  port = 6311,
  args = c(
    '--RS-enable-remote',
    '--no-save'),
  config.file = "/Rserv.conf")
