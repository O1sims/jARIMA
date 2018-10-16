package arima.api.controllers;

import arima.api.models.ArimaModel;

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
	public double[] calculateRArima(
			@Valid @RequestBody ArimaModel rArima)
					throws Exception {
		double[] out = {1, 2};
        return out; 
	}
}
