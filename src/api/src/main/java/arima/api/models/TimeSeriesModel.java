package arima.api.models;

public class TimeSeriesModel {
	
	private double[] tsData;
	private int forecastPeriod;

	public double[] getTSData() {
		return tsData;
	}

	public void setTSData(double[] tsdata) {
		this.tsData = tsdata;
	}
	
	public int getForecastPeriod() {
		return forecastPeriod;
	}

	public void setForecastPeriod(int forecastPeriod) {
		this.forecastPeriod = forecastPeriod;
	}
}