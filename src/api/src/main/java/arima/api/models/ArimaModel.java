package arima.api.models;

import arima.api.analytics.ArimaSolver;

/**
 * ARIMA model
 */
public class ArimaModel {

    private final ArimaParameterModel params;
    private final double[] data;
    private final int trainDataSize;
    private double rmse;
    private double aic;

    /**
     * Constructor for ArimaModel
     *
     * @param params ARIMA parameter
     * @param data original data
     * @param trainDataSize size of train data
     */
    public ArimaModel(ArimaParameterModel params, double[] data, int trainDataSize) {
        this.params = params;
        this.data = data;
        this.trainDataSize = trainDataSize;
    }
    
    /**
     * Getter for AIC.
     *
     * @return AIC for the ARIMA model
     */
    public double getAIC() {
        return this.aic;
    }

    /**
     * Setter for Root Mean-Squared Error
     *
     * @param rmse source Root Mean-Squared Error
     */
    public void setAIC(final double aic) {
        this.aic = aic;
    }

    /**
     * Getter for Root Mean-Squared Error.
     *
     * @return Root Mean-Squared Error for the ARIMA model
     */
    public double getRMSE() {
        return this.rmse;
    }

    /**
     * Setter for Root Mean-Squared Error
     *
     * @param rmse source Root Mean-Squared Error
     */
    public void setRMSE(final double rmse) {
        this.rmse = rmse;
    }

    /**
     * Getter for ARIMA parameters.
     *
     * @return ARIMA parameters for the model
     */
    public ArimaParameterModel getParams() {
        return params;
    }

    /**
     * Forecast data base on training data and forecast size.
     *
     * @param forecastSize size of forecast
     * @return forecast result
     */
    public ForecastResultModel forecast(final int forecastSize) {
        ForecastResultModel forecastResult = ArimaSolver
            .forecastARIMA(params, data, trainDataSize, trainDataSize + forecastSize);
        forecastResult.setAIC(this.aic);
        forecastResult.setRMSE(this.rmse);

        return forecastResult;
    }

}
