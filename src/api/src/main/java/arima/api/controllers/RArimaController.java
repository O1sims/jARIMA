package arima.api.controllers;

import arima.api.models.RArimaModel;

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
	@Value("${rserve.production}") @NotNull private boolean production;
	
	private final Logger LOGGER = LoggerFactory.getLogger(RArimaController.class);
	
	@RequestMapping(
			value = "/", 
			method = RequestMethod.POST)
	public double[] calculateRArima(
			@Valid @RequestBody RArimaModel rArima)
					throws Exception {
		LOGGER.info("Connecting to Rserve: {}:{}", this.hostname, this.port);
        this.connection = new RConnection(this.hostname, this.port);
        this.connection.assign("tsData", rArima.getTSData());
        LOGGER.info("Evaluating time-series data...");
        this.connection.voidEval("dffv <- data.frame(forecast::forecast(forecast::auto.arima(tsData), 1))");
        double[] out = {
        		this.connection.eval("dffv$Hi.95").asDouble(),
            	this.connection.eval("dffv$Lo.95").asDouble()
        };
        return out; 
	}
}
