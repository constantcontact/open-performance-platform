Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtrendreport',
    transactionData: null,

    onHistogramUserTimingDataLoaded: function(histogramData) {

        var customTimingsStore,
            customTimingChart,
            customTimingsChartData;

        customTimingsStore = this.getView()
            .getViewModel()
            .getStore('customTimings');

        var data = customTimingsStore.getData().items;

        // store data for later use
        this.transactionData = data;

        this.createRangeAreaHighchart(data);

    },

    getHighchartsSeries: function(data) {
        // get all names
        var names = new Set();
        data.forEach(function(d) {
            var obj = d.data;
            names.add(d.data.name);
        });

        // create object like this:
        /*
        [{
            series1: [ [1, 3, 8], [2, 5, 10], [7, 1, 12]],
            series2: [ [2, 4, 14], [5, 7] ],
            series3: [ [1, 8, 16], [4, 6, 8], [5, 1, 5], [9, 4, 10]]
        }]
        */

        var series = [];
        var colorIndex = 0;
        names.forEach((n) => {

            // set color and series
            var color = this.getColor(colorIndex);
            var lineSeries = {
                name: n,
                //plot: 'line',
                type: 'line',
                dataIndex: n + "-line",
                color: color,
                marker: { fillColor: color, lineWidth: 2, lineColor: color },
                zIndex: 1
            };
            var rangeSeries = {
                name: n + "-range",
                //   plot: 'arearange',
                type: 'arearange',
                dataIndex: n,
                color: color,
                fillOpacity: 0.4,
                lineWidth: 0,
                linkedTo: ':previous',
                zIndex: 0,
                marker: { enabled: false }
            };
            series.push(lineSeries);
            series.push(rangeSeries);
            colorIndex++;
        });
        return series;
    },

    createRangeAreaHighchart: function(data) {
        var series = this.getHighchartsSeries(data);
        var dataStore = this.getHighchartsUTData(data, "median");

        var chart = this.getCustomTimingsChart();
        chart.store.loadData([dataStore], false);
        chart.addSeries(series, false);
        chart.draw();

    },
    getCustomTimingsChart: function() {
        return this.getView().down('customtimingchart').down('highchart');
    },
    getHighchartsUTData: function(data, lineMetric) {
        var dataStore = Object();
        data.forEach((d) => {
            var obj = d.data;
            if (obj.min !== 0 && obj.max !== 0) {
                // add line value
                var lineValue = [obj.timePeriod, obj[lineMetric]];
                var lineKey = obj.name + "-line";
                if (!dataStore.hasOwnProperty(lineKey)) dataStore[lineKey] = []; // init if it doesn't exist
                dataStore[lineKey].push(lineValue);
                // add range value
                if (!dataStore.hasOwnProperty(obj.name)) dataStore[obj.name] = [];
                dataStore[obj.name].push([obj.timePeriod, obj.min, obj.max]);
            }
        })
        return dataStore;
    },

    // createExtChart: function(data) {
    //     var names = new Set();
    //     var fields = new Set();
    //     data.forEach(function(d) {
    //         var obj = d.data;
    //         names.add(obj.name);
    //         // not really needed, but probably good form
    //         fields.add(obj.name + "-min");
    //         fields.add(obj.name + "-max");
    //         fields.add(obj.name + "-median");
    //         fields.add(obj.name + "-median");
    //     });

    //     this.transactionNames = names;

    //     var rec = Object();
    //     data.forEach(function(d) {
    //         var obj = d.data;
    //         var timePeriod = obj.timePeriod / 1000;
    //         if (!rec.hasOwnProperty(timePeriod)) {
    //             rec[timePeriod] = Object();
    //         }
    //         var curRec = rec[timePeriod];
    //         curRec[obj.name + "-min"] = obj.min;
    //         curRec[obj.name + "-max"] = obj.max;
    //         curRec[obj.name + "-average"] = obj.average;
    //         curRec[obj.name + "-median"] = obj.median;
    //         rec[timePeriod] = curRec;

    //     });


    //     // need to get this to work... its currently showing up as an array
    //     var dataArr = [];
    //     for (key in rec) {
    //         var obj = rec[key];
    //         obj.timePeriod = key;
    //         dataArr.push(Object(obj));
    //     };
    //     console.log(dataArr);
    //     this.getView().down('customtimingchart').down('highcharts').store.loadData(dataArr, false);
    //     customTimingsStore.loadData(dataArr, false);


    //     // build out series
    //     var series = [];
    //     var colorIndex = 0;
    //     names.forEach((n) => {
    //         series.push(this.createUtSeries(n, "median", "line", this.getColor(colorIndex), null, true, false));
    //         // don't do this for 2 reasdons... 1 bug in extjs... can't set same color and two the charts get too crowded
    //         series.push(this.createUtSeries(n, "min", "scatter", this.getColor(colorIndex), 'square', false, true));
    //         series.push(this.createUtSeries(n, "max", "scatter", this.getColor(colorIndex), 'circle', false, true));
    //         colorIndex++;
    //     });
    //     var chart = this.getView().down('customtimingchart');
    //     chart.getAxes()[0].setFields(Array.from(fields)); // set fields --- not really needed
    //     chart.setSeries(series);
    //     chart.setStore(customTimingsStore);
    //     chart.redraw();
    // },

    getColor: function(index) {
        // 20 preferred colors for the charts that are easy to read
        var chartColors = ['#3366CC', '#DC3912', '#FF9900', '#109618', '#990099', '#3B3EAC', '#0099C6', '#DD4477', '#66AA00', '#B82E2E', '#316395', '#994499', '#22AA99', '#AAAA11', '#6633CC', '#E67300', '#8B0707', '#329262', '#5574A6', '#3B3EAC']
        if (index < 20) {
            return chartColors[index];
        } else {
            // over 20... just generate random color
            return '#' + Math.floor(Math.random() * 16777215).toString(16);
        }
    },

    // createUtSeries: function(transName, metric, seriesType, color, markerType, showInLegend, hidden) {
    //     return {
    //         type: seriesType,
    //         style: { stroke: color, lineWidth: 2 },
    //         // style: { lineWidth: 2 },
    //         xField: 'timePeriod',
    //         yField: transName + "-" + metric,
    //         hidden: hidden,
    //         showInLegend: showInLegend,
    //         marker: { type: markerType, radius: 4, lineWidth: 2, fill: 'white' }
    //     };
    // },

    onHistogramDataLoaded: function(histogramData) {
        var metricStore,
            defaultStore,
            wptTrendGrid,
            defaultStoreData,
            customTimingsStore;

        metricStore = this.getView()
            .getViewModel()
            .getStore('median');

        defaultStore = this.getView()
            .getViewModel()
            .getStore('histogramData');

        customTimingsStore = this.getView()
            .getViewModel()
            .getStore('customTimings');

        // load user timings store
        customTimingsStore.load();

        if (!metricStore.getProxy().getData()) {
            defaultStoreData = defaultStore.getProxy().getReader().rawData;

            metricStore.getProxy().setData(defaultStoreData);
            metricStore.load();
        } else {
            metricStore.reload();
        }

        this.getView().down('wpttrendchart').setStore(metricStore);
        this.getView().down('wpttrendchart').setTitle('WPT Trend - median');

    },

    onUTMetricChange: function(newMetric) {
        var chart = this.getCustomTimingsChart();
        var dataStore = this.getHighchartsUTData(this.transactionData, newMetric);
        chart.store.loadData([dataStore], false);
        chart.refresh();
    },

    // onUTMetricChange(newMetric) {

    //     var view = this.getView();

    //     // build out series
    //     var series = [];
    //     var colorIndex = 0;
    //     this.transactionNames.forEach((n) => {
    //         series.push(this.createUtSeries(n, newMetric, "line", this.getColor(colorIndex), null, true));
    //         // don't do this for 2 reasdons... 1 bug in extjs... can't set same color and two the charts get too crowded
    //         // series.push(this.createUtSeries(n, "min", "scatter", this.getColor(colorIndex), 'square', false));
    //         // series.push(this.createUtSeries(n, "max", "scatter", this.getColor(colorIndex), 'circle', false));
    //         colorIndex++;
    //     });

    //     // create data that is min, median, max, and average all with the trans name
    //     // then on button clicks, show and hide average and median series
    //     var chart = view.down('customtimingchart');
    //     var oldSeries = chart.getSeries();
    //     console.log("starting series removal");
    //     oldSeries.forEach((s) => {
    //         console.log(s);
    //         chart.removeSeries(s.getId());
    //         console.log("removed - " + s.getId());
    //     });
    //     console.log('removed series');
    //     chart.setSeries(series);

    //     // view.setStore(store);
    //     // view.setTitle('Custom Timings - ' + button.getText());
    // },


    updateUrlTabState: function(tab) {
        this.getView()
            .up('uxtabpanel')
            .getController()
            .updateUrlTabState(tab.getTitle(), false);
    },
    listen: {
        controller: {
            'customtimingchart': {
                // names of events fired by any controller
                utMetricChange: 'onUTMetricChange'
            }
        }
    }
});