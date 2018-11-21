package arima.api.controllers;

import arima.api.models.TimeSeriesModel;
import arima.api.models.RArimaResultModel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/r-arima")
public class RArimaController {
	
	private RConnection connection;
	
	@Value("${rserve.port}") @NotNull private int port;
	@Value("${rserve.hostname}") @NotNull private String hostname;
	
	private final Logger LOGGER = LoggerFactory.getLogger(RArimaController.class);
	
	@RequestMapping(
			value = "/",
			method = RequestMethod.POST)
	public RArimaResultModel calculateRArima(
			@Valid @RequestBody TimeSeriesModel rArima)
					throws Exception {
		LOGGER.info("Connecting to Rserve: {}:{}", this.hostname, this.port);
        this.connection = new RConnection(this.hostname, this.port);
        this.connection.assign("tsData", rArima.getTSData());
        this.connection.assign("forecastPeriod", String.valueOf(rArima.getForecastPeriod()));
        LOGGER.info("Evaluating time-series data...");
        this.connection.voidEval(
        		"dffv <- data.frame(forecast::forecast(forecast::auto.arima(tsData), forecastPeriod))");
        
        RArimaResultModel forecastResult = new RArimaResultModel(
        		this.connection.eval("dffv$Point.Forecast").asDoubles(), 
        		this.connection.eval("dffv$Hi.95").asDoubles(), 
        		this.connection.eval("dffv$Lo.95").asDoubles());
        
        return forecastResult;
	}
}
