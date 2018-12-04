package arima.api.analytics;

import arima.api.models.ArimaModel;
import arima.api.models.ArimaParameterModel;
import arima.api.models.ForecastResultModel;

public final class ArimaSolver {

    private static final int maxIterationForHannanRissanen = 5;

    private ArimaSolver() {}

    public static double[] forecastARMA(final ArimaParameterModel params, final double[] dataStationary,
        final int startIndex, final int endIndex) {

        final int train_len = startIndex;
        final int total_len = endIndex;
        final double[] errors = new double[total_len];
        final double[] data = new double[total_len];
        System.arraycopy(dataStationary, 0, data, 0, train_len);
        final int forecast_len = endIndex - startIndex;
        final double[] forecasts = new double[forecast_len];
        final int _dp = params.getDegreeP();
        final int _dq = params.getDegreeQ();
        final int start_idx = (_dp > _dq) ? _dp : _dq;

        for (int j = 0; j < start_idx; ++j) {
            errors[j] = 0;
        }
        // populate errors and forecasts
        for (int j = start_idx; j < train_len; ++j) {
            final double forecast = params.forecastOnePointARMA(data, errors, j);
            final double error = data[j] - forecast;
            errors[j] = error;
        }
        // now we can forecast
        for (int j = train_len; j < total_len; ++j) {
            final double forecast = params.forecastOnePointARMA(data, errors, j);
            data[j] = forecast;
            errors[j] = 0;
            forecasts[j - train_len] = forecast;
        }
        // return forecasted values
        return forecasts;
    }

    public static ForecastResultModel forecastARIMA(final ArimaParameterModel params, final double[] data,
        final int forecastStartIndex, final int forecastEndIndex) {

        if (!checkARIMADataLength(params, data, forecastStartIndex, forecastEndIndex)) {
            final int initialConditionSize = params.d + params.D * params.m;
            throw new RuntimeException(
                "not enough data for ARIMA. needed at least " + initialConditionSize +
                    ", have " + data.length + ", startIndex=" + forecastStartIndex + ", endIndex="
                    + forecastEndIndex);
        }

        final int forecast_length = forecastEndIndex - forecastStartIndex;
        final double[] forecast = new double[forecast_length];
        final double[] data_train = new double[forecastStartIndex];
        System.arraycopy(data, 0, data_train, 0, forecastStartIndex);

        //=======================================
        // DIFFERENTIATE
        final boolean hasSeasonalI = params.D > 0 && params.m > 0;
        final boolean hasNonSeasonalI = params.d > 0;
        double[] data_stationary = differentiate(params, data_train, hasSeasonalI,
            hasNonSeasonalI);  // currently un-centered
        // END OF DIFFERENTIATE
        //==========================================

        //=========== CENTERING ====================
        final double mean_stationary = Integrator.computeMean(data_stationary);
        Integrator.shift(data_stationary, (-1) * mean_stationary);
        final double dataVariance = Integrator.computeVariance(data_stationary);
        //==========================================

        //==========================================
        // FORECAST
        final double[] forecast_stationary = forecastARMA(params, data_stationary,
            data_stationary.length,
            data_stationary.length + forecast_length);

        final double[] data_forecast_stationary = new double[data_stationary.length
            + forecast_length];

        System.arraycopy(data_stationary, 0, data_forecast_stationary, 0, data_stationary.length);
        System.arraycopy(forecast_stationary, 0, data_forecast_stationary, data_stationary.length,
            forecast_stationary.length);
        // END OF FORECAST
        //==========================================

        //=========== UN-CENTERING =================
        Integrator.shift(data_forecast_stationary, mean_stationary);
        //==========================================

        //===========================================
        // INTEGRATE
        double[] forecast_merged = integrate(params, data_forecast_stationary, hasSeasonalI,
            hasNonSeasonalI);
        // END OF INTEGRATE
        //===========================================
        System.arraycopy(forecast_merged, forecastStartIndex, forecast, 0, forecast_length);

        return new ForecastResultModel(forecast, dataVariance);
    }

    public static ArimaModel estimateARIMA(final ArimaParameterModel params, final double[] data,
        final int forecastStartIndex, final int forecastEndIndex) {

        if (!checkARIMADataLength(params, data, forecastStartIndex, forecastEndIndex)) {
            final int initialConditionSize = params.d + params.D * params.m;
            throw new RuntimeException(
                "not enough data for ARIMA. needed at least " + initialConditionSize +
                    ", have " + data.length + ", startIndex=" + forecastStartIndex + ", endIndex="
                    + forecastEndIndex);
        }

        final int forecast_length = forecastEndIndex - forecastStartIndex;
        final double[] data_train = new double[forecastStartIndex];
        System.arraycopy(data, 0, data_train, 0, forecastStartIndex);

        //=======================================
        // DIFFERENTIATE
        final boolean hasSeasonalI = params.D > 0 && params.m > 0;
        final boolean hasNonSeasonalI = params.d > 0;
        double[] data_stationary = differentiate(params, data_train, hasSeasonalI,
            hasNonSeasonalI);  // currently un-centered
        // END OF DIFFERENTIATE
        //==========================================

        //=========== CENTERING ====================
        final double mean_stationary = Integrator.computeMean(data_stationary);
        Integrator.shift(data_stationary, (-1) * mean_stationary);
        //==========================================

        //==========================================
        // FORECAST
        HannanRissanen
            .estimateARMA(data_stationary, params, forecast_length,
                maxIterationForHannanRissanen);

        return new ArimaModel(params, data, forecastStartIndex);
    }

    private static double[] differentiate(ArimaParameterModel params, double[] trainingData,
        boolean hasSeasonalI, boolean hasNonSeasonalI) {
        double[] dataStationary;  // currently un-centered
        if (hasSeasonalI && hasNonSeasonalI) {
            params.differentiateSeasonal(trainingData);
            params.differentiateNonSeasonal(params.getLastDifferenceSeasonal());
            dataStationary = params.getLastDifferenceNonSeasonal();
        } else if (hasSeasonalI) {
            params.differentiateSeasonal(trainingData);
            dataStationary = params.getLastDifferenceSeasonal();
        } else if (hasNonSeasonalI) {
            params.differentiateNonSeasonal(trainingData);
            dataStationary = params.getLastDifferenceNonSeasonal();
        } else {
            dataStationary = new double[trainingData.length];
            System.arraycopy(trainingData, 0, dataStationary, 0, trainingData.length);
        }

        return dataStationary;
    }

    private static double[] integrate(ArimaParameterModel params, double[] dataForecastStationary,
        boolean hasSeasonalI, boolean hasNonSeasonalI) {
        double[] forecast_merged;
        if (hasSeasonalI && hasNonSeasonalI) {
            params.integrateSeasonal(dataForecastStationary);
            params.integrateNonSeasonal(params.getLastIntegrateSeasonal());
            forecast_merged = params.getLastIntegrateNonSeasonal();
        } else if (hasSeasonalI) {
            params.integrateSeasonal(dataForecastStationary);
            forecast_merged = params.getLastIntegrateSeasonal();
        } else if (hasNonSeasonalI) {
            params.integrateNonSeasonal(dataForecastStationary);
            forecast_merged = params.getLastIntegrateNonSeasonal();
        } else {
            forecast_merged = new double[dataForecastStationary.length];
            System.arraycopy(dataForecastStationary, 0, forecast_merged, 0,
                dataForecastStationary.length);
        }

        return forecast_merged;
    }

    public static double computeRMSE(final double[] left, final double[] right,
        final int leftIndexOffset,
        final int startIndex, final int endIndex) {

        double square_sum = 0.0;
        for (int i = startIndex; i < endIndex; ++i) {
            final double error = left[i + leftIndexOffset] - right[i];
            square_sum += error * error;
        }

        return Math.sqrt(square_sum / (double) (endIndex - startIndex));
    }
    
    public static double computeAIC(final double[] left, final double[] right,
        final int leftIndexOffset,
        final int startIndex, final int endIndex) {

        double error_sum = 0.0;
        for (int i = startIndex; i < endIndex; ++i) {
            final double error = left[i + leftIndexOffset] - right[i];
            error_sum += Math.abs(error);
        }
        if (error_sum == 0.0) {
        	return 0;
        } else {
        	return (endIndex - startIndex) * Math.log(error_sum) + 2;
        }
    }

    public static double computeRMSEValidation(final double[] data,
        final double testDataPercentage, ArimaParameterModel params) {

        int testDataLength = (int) (data.length * testDataPercentage);
        int trainingDataEndIndex = data.length - testDataLength;

        final ArimaModel result = estimateARIMA(params, data, trainingDataEndIndex,
            data.length);

        final double[] forecast = result.forecast(testDataLength).getForecast();

        return computeRMSE(data, forecast, trainingDataEndIndex, 0, forecast.length);
    }

    public static double computeAICValidation(final double[] data,
        final double testDataPercentage, ArimaParameterModel params) {

        int testDataLength = (int) (data.length * testDataPercentage);
        int trainingDataEndIndex = data.length - testDataLength;

        final ArimaModel result = estimateARIMA(params, data, trainingDataEndIndex,
            data.length);

        final double[] forecast = result.forecast(testDataLength).getForecast();

        return computeAIC(data, forecast, trainingDataEndIndex, 0, forecast.length);
    }

    public static double setSigma2AndPredicationInterval(final ArimaParameterModel params,
        final ForecastResultModel forecastResult, final int forecastSize) {

        final double[] coeffs_AR = params.getCurrentARCoefficients();
        final double[] coeffs_MA = params.getCurrentMACoefficients();
        return forecastResult
            .setConfInterval(ForecastUtil.confidence_constant_95pct,
                ForecastUtil.getCumulativeSumOfCoeff(
                    ForecastUtil.ARMAtoMA(coeffs_AR, coeffs_MA, forecastSize)));
    }

    private static boolean checkARIMADataLength(ArimaParameterModel params, double[] data, int startIndex,
        int endIndex) {
        boolean result = true;

        final int initialConditionSize = params.d + params.D * params.m;

        if (data.length < initialConditionSize || startIndex < initialConditionSize
            || endIndex <= startIndex) {
            result = false;
        }

        return result;
    }
}
