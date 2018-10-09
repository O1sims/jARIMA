package arima.api.controllers;

import arima.api.models.RArimaModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/rarima")
public class RArimaController {

  @RequestMapping(
		  value = "/",
		  method = RequestMethod.POST)
  public void calculateRArima() {
    // Go do something here...


  }
}
