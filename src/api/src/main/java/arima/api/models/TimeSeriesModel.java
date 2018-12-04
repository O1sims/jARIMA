package arima.api.models;

public class TimeSeriesModel {

	private double[] tsData;
	private int forecastPeriod;

	/**
	 * Constructor for TimeSeriesModel
	 *
	 * @param params ARIMA parameter
	 * @param data original data
	 * @param trainDataSize size of train data
	 */
	public TimeSeriesModel(double[] tsData, int forecastPeriod) {
			this.forecastPeriod = forecastPeriod;
			this.tsData = tsData;
	}

	/**
	 * Getter for Time Series Data.
	 *
	 * @return Time Series data used for the model
	 */
	public double[] getTSData() {
		return this.tsData;
	}

	/**
	 * Setter for Time Series Data.
	 *
	 * @return Time Series data used for the model
	 */
	public void setTSData(double[] tsData) {
		this.tsData = tsData;
	}

	/**
	 * Getter for Forecast Period.
	 *
	 * @return Forecast period used in the model
	 */
	public int getForecastPeriod() {
		return this.forecastPeriod;
	}

	/**
	 * Getter for Forecast Period.
	 *
	 * @return Forecast period used in the model
	 */
	public void setForecastPeriod(int forecastPeriod) {
		this.forecastPeriod = forecastPeriod;
	}
}
