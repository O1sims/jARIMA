package arima.api.analytics.timeseries.arima;

import arima.api.analytics.timeseries.arima.struct.ArimaModel;
import arima.api.analytics.timeseries.arima.struct.ArimaParams;
import arima.api.analytics.timeseries.arima.struct.ForecastResult;
import arima.api.analytics.timeseries.timeseriesutil.ForecastUtil;

/**
 * ARIMA implementation
 */
public final class Arima {

    private Arima() {
    } // pure static class

    /**
     * Raw-level ARIMA forecasting function.
     *
     * @param data UNMODIFIED, list of double numbers representing time-series with constant time-gap
     * @param forecastSize integer representing how many data points AFTER the data series to be
     *        forecasted
     * @param params ARIMA parameters
     * @return a ForecastResult object, which contains the forecasted values and/or error message(s)
     */
    public static ForecastResult forecast_arima(final double[] data, final int forecastSize) {

        try {
        	final ArimaParams paramsForecast = new ArimaParams(3, 0, 3, 1, 1, 0, 0);
            // estimate ARIMA model parameters for forecasting
            final ArimaModel fittedModel = ArimaSolver.estimateARIMA(
                paramsForecast, data, data.length, data.length + 1);

            // compute RMSE to be used in confidence interval computation
            final double rmseValidation = ArimaSolver.computeRMSEValidation(
                data, ForecastUtil.testSetPercentage, paramsForecast);
            fittedModel.setRMSE(rmseValidation);
            final ForecastResult forecastResult = fittedModel.forecast(forecastSize);

            // populate confidence interval
            forecastResult.setSigma2AndPredicationInterval(fittedModel.getParams());

            // successfully built ARIMA model and its forecast
            return forecastResult;

        } catch (final Exception ex) {
            // failed to build ARIMA model
            throw new RuntimeException("Failed to build ARIMA forecast: " + ex.getMessage());
        }
    }
}
