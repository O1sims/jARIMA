import { Component, OnInit } from '@angular/core';

import { HomeService } from './home.service';

declare let Chart: any;

@Component({
  selector: 'home',
  templateUrl: './home/home.component.html',
  providers: [HomeService]
})

export class HomeComponent implements OnInit {

  constructor(
    private homeService: HomeService) {};

  maxValue:number = 100;
  dataLength:number = 10;

  postBody:object = {
    "forecastPeriod": 1,
    "tsdata": []
  };

  postBodyString:string;

  rArimaResults:object = {
    "forecast": [],
    "lowerBound": [],
    "upperBound": []
  };

  jArimaResults:object = {
    "forecast": [],
    "lowerBound": [],
    "upperBound": []
  };

  ngOnInit() {
    this.generateTimeSeriesData();
    this.stringify();
    this.generateChart();
  };

  generateChart() {
    var ctx = document.getElementById('TimeSeriesChart');
    var chart = new Chart(ctx, {
      // The type of chart we want to create
      "type": 'line',

      // The data for our dataset
      "data": {
      "labels": Array.apply(null, {length: this.dataLength}).map(Number.call, Number),
      "datasets": [{
          "label": "Time series data",
          "fill": false,
          "lineTension": 0.1,
          "data": this.postBody['tsdata'],
          "borderColor": "rgb(75, 192, 192)"
        }]
    },

    // Configuration options go here
    options: {}
});
  };

  stringify() {
    this.postBodyString = JSON.stringify(
      this.postBody);
  };

  generateTimeSeriesData() {
    this.postBody['tsdata'] = Array.from(
      { length: this.dataLength },
      () => Math.floor(Math.random() * this.maxValue));
    this.stringify();
    this.generateChart();
  };

  setFeature(event, feature) {
    var value = event['target']['value'];
    if (feature=="maxValue") {
      this.maxValue = value;
    } else if (feature=="dataLength") {
      this.dataLength = value;
    } else if (feature=="forecastPeriod") {
      this.postBody['forecastPeriod'] = value;
      this.stringify();
    };
  };

  sendToARIMA() {
    // Send to R component
    this.homeService.getARIMAResults(
      this.postBody['forecastPeriod'],
      this.postBody['tsdata'],
      'r-arima').subscribe(
        rArimaResults => {
          this.rArimaResults = rArimaResults;
          // Send to Java component
          this.homeService.getARIMAResults(
          this.postBody['forecastPeriod'],
          this.postBody['tsdata'],
            'j-arima').subscribe(
              jArimaResults => {
                this.jArimaResults = jArimaResults;
              })
        });
  };
}
