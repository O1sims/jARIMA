package arima.api.models;

public class ArimaModel {
	
	private double[] tsdata;
	private int forecastPeriod;

	public double[] getTSData() {
		return tsdata;
	}

	public void setTSData(double[] tsdata) {
		this.tsdata = tsdata;
	}
	
	public int getForecastPeriod() {
		return forecastPeriod;
	}

	public void setForecastPeriod(int forecastPeriod) {
		this.forecastPeriod = forecastPeriod;
	}
}