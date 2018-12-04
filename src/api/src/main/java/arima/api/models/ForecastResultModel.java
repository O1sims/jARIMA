package arima.api.models;

import arima.api.analytics.ArimaSolver;

/**
 * ARIMA Forecast Result
 */
public class ForecastResultModel {

    private double[] forecast;
    private double[] upperBound;
    private double[] lowerBound;
    private final double dataVariance;
    
    private double modelAIC;
    private double modelRMSE;
    private double maxNormalizedVariance;

    /**
     * Constructor for ForecastResult
     */
    public ForecastResultModel(final double[] pForecast, final double pDataVariance) {

        this.forecast = pForecast;

        this.upperBound = new double[pForecast.length];
        System.arraycopy(pForecast, 0, upperBound, 0, pForecast.length);

        this.lowerBound = new double[pForecast.length];
        System.arraycopy(pForecast, 0, lowerBound, 0, pForecast.length);

        this.dataVariance = pDataVariance;
        
        this.modelAIC = -1;
        this.modelRMSE = -1;
        this.maxNormalizedVariance = -1;
    }

    /**
     * Compute normalized variance
     */
    private double getNormalizedVariance(final double v) {
        if (v < -0.5 || dataVariance < -0.5) {
            return -1;
        } else if (dataVariance < 0.0000001) {
            return v;
        } else {
            return Math.abs(v / dataVariance);
        }
    }

    public double getAIC() {
		return modelAIC;
	}
    
    void setAIC(double aic) {
		this.modelAIC = aic;
	}

	/**
     * Getter for Root Mean-Squared Error
     *
     * @return Root Mean-Squared Error
     */
    public double getRMSE() {
        return this.modelRMSE;
    }

    /**
     * Setter for Root Mean-Squared Error
     *
     * @param rmse Root Mean-Squared Error
     */
    void setRMSE(double rmse) {
        this.modelRMSE = rmse;
    }

    /**
     * Getter for Max Normalized Variance
     *
     * @return Max Normalized Variance
     */
    public double getMaxNormalizedVariance() {
        return maxNormalizedVariance;
    }

    /**
     * Compute and set confidence intervals
     *
     * @param constant confidence interval constant
     * @param cumulativeSumOfMA cumulative sum of MA coefficients
     * @return Max Normalized Variance
     */
    public double setConfInterval(final double constant, final double[] cumulativeSumOfMA) {
        double maxNormalizedVariance = -1.0;
        double bound = 0;
        for (int i = 0; i < forecast.length; i++) {
            bound = constant * modelRMSE * cumulativeSumOfMA[i];
            this.upperBound[i] = this.forecast[i] + bound;
            this.lowerBound[i] = this.forecast[i] - bound;
            final double normalizedVariance = getNormalizedVariance(Math.pow(bound, 2));
            if (normalizedVariance > maxNormalizedVariance) {
                maxNormalizedVariance = normalizedVariance;
            }
        }
        return maxNormalizedVariance;
    }

    /**
     * Compute and set Sigma2 and prediction confidence interval.
     *
     * @param params ARIMA parameters from the model
     */
    public void setSigma2AndPredicationInterval(ArimaParameterModel params) {
        maxNormalizedVariance = ArimaSolver
            .setSigma2AndPredicationInterval(params, this, forecast.length);
    }

    /**
     * Getter for forecast data
     *
     * @return forecast data
     */
    public double[] getForecast() {
        return forecast;
    }
    

    /**
     * Getter for upper confidence bounds
     *
     * @return array of upper confidence bounds
     */
    public double[] getupperBound() {
        return upperBound;
    }
    
    /**
     * Setter for upper bound
     */
    public void setupperBound(double[] upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * Getter for lower confidence bounds
     *
     * @return array of lower confidence bounds
     */
    public double[] getlowerBound() {
        return lowerBound;
    }
    
    /**
     * Setter for lower bound
     */
    public void setlowerBound(double[] lowerBound) {
        this.lowerBound = lowerBound;
    }
    
}
