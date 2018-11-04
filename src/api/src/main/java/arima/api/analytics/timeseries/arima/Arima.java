package arima.api.analytics.timeseries.arima;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

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
     * @return a ForecastResult object, which contains the forecasted values and/or error message(s)
     */
    public static ForecastResult forecast_arima(final double[] data, final int forecastSize) {

        try {
        	// establish array to store forecast results
        	ForecastResult currentModel = null;
        	
        	// initial models (Hyndman and Khandakar, 2008:11)
        	// for non-seasonal data
        	int[] params = {2, 2, 0, 0, 1, 0, 0, 1};
        	List<Integer> list = Arrays
        			.stream(params)
        			.boxed()
        			.collect(Collectors.toList());
        	
        	// loop over potential models
        	for (int x = 0; x <=(params.length-2)/2; x++) {
        		currentModel = forecast(
        				currentModel, data, forecastSize, list.get(x*2), list.get((x*2)+1));
        	}

            // successfully built ARIMA model and its forecast
            return currentModel;

        } catch (final Exception ex) {
            // failed to build ARIMA model
            throw new RuntimeException("Failed to build ARIMA forecast: " + ex.getMessage());
        }
        
    }
    
    private static ForecastResult forecast(ForecastResult currentModel, final double[] data, 
    		final int forecastSize, int p, int q) {
    	
    	// set the initial parameters for (p, d, q, P, D, Q, m)
		final ArimaParams paramsForecast = new ArimaParams(
				p, 0, q, 1, 0, 1, 1);
    	
        // estimate ARIMA model parameters for forecasting
        final ArimaModel fittedModel = ArimaSolver.estimateARIMA(
            paramsForecast, data, data.length, data.length + 1);

        // compute RMSE to be used in confidence interval computation
        final double rmseValidation = ArimaSolver.computeRMSEValidation(
            data, ForecastUtil.testSetPercentage, paramsForecast);
        
        if (currentModel == null || rmseValidation < currentModel.getRMSE()) {
        	// set the RMSE
            fittedModel.setRMSE(rmseValidation);
            
            // run forecast model
        	final ForecastResult forecastResult = fittedModel.forecast(forecastSize);
            
            // populate confidence interval
            forecastResult.setSigma2AndPredicationInterval(fittedModel.getParams());
            
            // add to forecast results
            return forecastResult;
        }
        
        return currentModel;
    }
}
