package arima.api.controllers;

import arima.api.models.ArimaModel;
import arima.api.analytics.timeseries.arima.Arima;
import arima.api.analytics.timeseries.arima.struct.ForecastResult;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/j-arima")
public class JArimaController {
	
	@RequestMapping(
			value = "/", 
			method = RequestMethod.POST)
	public ForecastResult calculateRArima(
			@Valid @RequestBody ArimaModel rArima)
					throws Exception {
		
		ForecastResult forecastResult = Arima.forecast_arima(
				rArima.getTSData(), rArima.getForecastPeriod());
		
		return forecastResult; 
	}
}
