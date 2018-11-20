package arima.api.models;


/**
 * R ARIMA Forecast Result
 */
public class RArimaResultModel {

    private double[] forecast;
    private double[] upperBound;
    private double[] lowerBound;

    /**
     * Constructor for RArimaResultModel
     */
    public RArimaResultModel(final double[] forecast, 
    		final double[] upperBound, final double[] lowerBound) {

        this.forecast = forecast;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
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
     * Setter for forecast data
     */
    public void setForecast(double[] forecast) {
        this.forecast = forecast;
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
