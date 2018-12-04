package arima.api.analytics;

import arima.api.models.ArimaModel;
import arima.api.models.ArimaParameterModel;
import arima.api.models.ForecastResultModel;

public final class Arima {

    private Arima() {} 

    public static ForecastResultModel forecast_arima(final double[] data, final int forecastSize) {

        try {
        	// establish array to store forecast results
        	ForecastResultModel currentModel = null;
        	
        	// loop over initial models (Hyndman and Khandakar, 2008:11)
        	// and select best model
        	for (int p = 0; p <= 2; p++) {
        		for (int q = 0; q <= 2; q++) {
        			currentModel = forecast(
        					currentModel, data, forecastSize, p, q, 1, 1);
        		}
        	}

            // successfully build ARIMA model and its forecast
            return currentModel;

        } catch (final Exception ex) {
            // failed to build ARIMA model
            throw new RuntimeException("Failed to build ARIMA forecast: " + ex.getMessage());
        }
        
    }
    
    private static ForecastResultModel forecast(ForecastResultModel currentModel, final double[] data, 
    		final int forecastSize, int p, int q, int P, int Q) {
    	
    	// set the initial parameters for (p, d, q, P, D, Q, m)
		final ArimaParameterModel paramsForecast = new ArimaParameterModel(
				p, 0, q, P, 0, Q, 1);
    	
        // estimate ARIMA model parameters for forecasting
        final ArimaModel fittedModel = ArimaSolver.estimateARIMA(
            paramsForecast, data, data.length, data.length + 1);

        // compute RMSE to be used in confidence interval computation
        final double aicValue = ArimaSolver.computeAICValidation(
            data, ForecastUtil.testSetPercentage, paramsForecast);
        
        if (currentModel == null || aicValue < currentModel.getAIC()) {
        	// set the AIC
            fittedModel.setAIC(aicValue);
            
            // compute RMSE to be used in confidence interval computation
            final double rmseValue = ArimaSolver.computeRMSEValidation(
                data, ForecastUtil.testSetPercentage, paramsForecast);
            
            // set the RMSE
            fittedModel.setRMSE(rmseValue);
            
            // run forecast model
        	final ForecastResultModel forecastResult = fittedModel.forecast(forecastSize);
            
            // populate confidence interval
            forecastResult.setSigma2AndPredicationInterval(fittedModel.getParams());
            
            // return new forecast model
            return forecastResult;
        }
        
        // return the existing model
        return currentModel;
    }
}
