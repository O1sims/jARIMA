import { Http, Response, Request, Headers, RequestOptions, RequestMethod } from "@angular/http";
import { Injectable } from '@angular/core';

import 'rxjs/add/operator/map';


@Injectable()
export class HomeService {
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
			url: '/api/' + model + '/',
      body: this.constructRequest(
        forecastPeriod,
        timeSeriesData)
		});
		return this.http.request(
      new Request(requestoptions))
		.map(res => res.json());
  };

};
