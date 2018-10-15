package arima.api.controllers;

import arima.api.models.RArimaModel;
import arima.api.services.RArimaService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@RestController
@RequestMapping("/r-arima")
public class RArimaController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(RArimaController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void calculateRArima(
			@Valid @RequestBody RArimaModel rArima,
			RArimaService rArimaService) 
					throws REXPMismatchException, REngineException {
		
		LOGGER.info("Parsing out TS Data");
		int[] data = rArima.getTSData();
		
		LOGGER.info("Sending off to arima function...");
		double[] something = rArimaService.performRArima(data);
		
		this.LOGGER.info(something.toString());

  }
}
