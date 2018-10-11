package arima.api.controllers;

import arima.api.models.RArimaModel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@RestController
@RequestMapping("/r-arima")
public class RArimaController {

  @RequestMapping(
		  value = "/",
		  method = RequestMethod.POST)
  public void calculateRArima(@Valid
		  @RequestBody RArimaModel rArima) {
	  // Go do something here...


  }
}
