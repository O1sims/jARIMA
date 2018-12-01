import { Http, Response, Request, Headers, RequestOptions, RequestMethod } from "@angular/http";
import { environment } from '../environment/environment';
import { Injectable } from '@angular/core';

import 'rxjs/add/operator/map';


@Injectable()
export class HomeService {
  api:string = environment.API_HOST + ":" +
  environment.API_PORT;

  constructor(
    private http: Http) {
	};

  constructRequest(forecastPeriod, timeSeriesData) {
    return {
      "forecastPeriod": forecastPeriod,
      "tsdata": timeSeriesData
    };
  };

  getARIMAResults(forecastPeriod, timeSeriesData, model) {
    var requestoptions = new RequestOptions({
			method: RequestMethod.Post,
			url: this.api + '/api/' + model + '/',
      body: this.constructRequest(
        forecastPeriod,
        timeSeriesData)
		});
		return this.http.request(
      new Request(requestoptions))
		.map(res => res.json());
  };

};
