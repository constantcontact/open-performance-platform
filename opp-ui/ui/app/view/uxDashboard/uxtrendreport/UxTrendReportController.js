Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtrendreport',
    // default settings
    defaultLineMetric: 'median',
    pageLoadChart: {
        data: null,
        metrics: ['ttfb', 'visuallyComplete', 'speedIndex'],
        dateField: 'completedDate',
        title: 'Page Load Time Metrics'
    },
    userTimingChart: {
        data: null,
        dateField: 'timePeriod',
        title: 'Custom User Timings'
    },
    // listener for when the ajax call returns with the page load data
    onPageLoadChartDataLoad: function(histogramData) {
        // get data and save for later use
        this.pageLoadChart.data = histogramData.data.items;
        // start loading user timings store
        this.getView().getViewModel().getStore('customTimings').load();
        // create page load chart
        this.createPageLoadChart(this.pageLoadChart.data);
    },
    // listener for when someone clicks the median or average button
    onPLMetricChange: function(newMetric) {
        var chart = this.getPageLoadTimingsChart();
        var dataStore = this.getPageLoadData(this.pageLoadChart.data, newMetric, this.pageLoadChart.dateField, this.pageLoadChart.metrics);
        chart.store.loadData([dataStore], false);
        chart.up('panel').setTitle(this.pageLoadChart.title + ' - ' + newMetric);

        chart.refresh();
    },

    // =========== page load chart
    // main method that creates the chart
    createPageLoadChart: function(data) {
        var series = this.buildAreaRangeLineSeries(this.pageLoadChart.metrics);
        var dataStore = this.getPageLoadData(data, this.defaultLineMetric, this.pageLoadChart.dateField, this.pageLoadChart.metrics);
        var chart = this.getPageLoadTimingsChart();
        chart.addSeries(series, false);
        chart.store.loadData([dataStore], false);
        chart.up('panel').setTitle(this.pageLoadChart.title + ' - ' + this.defaultLineMetric);
        chart.draw();

    },
    
    // get datastore for chart
    getPageLoadData: function(data, lineMetric, dateField, names) {
        var dataStore = Object();
        data.forEach((d) => {
            var obj = d.data;
            names.forEach((n) => {
                if (obj[n].min !== 0 && obj[n].max !== 0) {
                    // e.g. - obj.completedDate, obj.ttfb.median
                    var lineValue = [obj[dateField], obj[n][lineMetric]];
                    var lineKey = n + "-line";
                    if (!dataStore.hasOwnProperty(lineKey)) dataStore[lineKey] = []; // init if it doesn't exist
                    dataStore[lineKey].push(lineValue);
                    // add range value
                    if (!dataStore.hasOwnProperty(n)) dataStore[n] = [];
                    dataStore[n].push([obj[dateField], obj[n].min, obj[n].max]);
                }
            });
        });
        return dataStore;
    },
    // get chart
    getPageLoadTimingsChart: function() {
        return this.getView().down('wpttrendchart').down('highchart');
    },
    //// ---- end page load chart ---------


    //////////// ---------- CUSTOM USER TIMINGS ------------

    // listener for when median or average button is clicked
    onUTMetricChange: function(newMetric) {
        var chart = this.getUserTimingChart();
        var dataStore = this.getUserTimingData(this.userTimingChart.data, newMetric, this.userTimingChart.dateField);
        chart.store.loadData([dataStore], false);
        chart.up('panel').setTitle(this.userTimingChart.title + ' - ' + newMetric);
        chart.refresh();
    },
    // callback for when ajax returns for user timing data
    onUserTimingDataLoad: function() {
        this.userTimingChart.data = this.getView().getViewModel().getStore('customTimings').getData().items;
        if(this.userTimingChart.data.length === 0) {
            // no user timing data - hide chart
            this.getUserTimingChart().up('panel').hide();
        } else {
            this.createUserTimingChart(this.userTimingChart.data);
        }
    },
    // format data to fit the highcharts store and match the series names
    getUserTimingData: function(data, lineMetric, dateField) {
        var dataStore = Object();
        data.forEach((d) => {
            var obj = d.data;
            if (obj.min !== 0 && obj.max !== 0) {
                // add line value
                var lineValue = [obj[dateField], obj[lineMetric]];
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
    // get all names for the series
    getUserTimingSeries: function(data) {
        // get all names
        var names = new Set();
        data.forEach(function(d) {
            var obj = d.data;
            names.add(d.data.name);
        });
        return this.buildAreaRangeLineSeries(names);
    },
    // main method to create the chart
    createUserTimingChart: function(data) {
        var series = this.getUserTimingSeries(data);
        var dataStore = this.getUserTimingData(data, this.defaultLineMetric, this.userTimingChart.dateField);

        var chart = this.getUserTimingChart();
        chart.store.loadData([dataStore], false);
        chart.addSeries(series, false);
        chart.up('panel').setTitle(this.userTimingChart.title + ' - ' +  this.defaultLineMetric);
        chart.draw();

    },
    // get the chart
    getUserTimingChart: function() {
        return this.getView().down('customtimingchart').down('highchart');
    },
    // ---------- end custom user timings ---------

    // build area range line series for both charts
    buildAreaRangeLineSeries: function(names) {
        var series = [];
        var colorIndex = 0;
        names.forEach((n) => {
            // set color and series
            var color = this.getColor(colorIndex);
            var lineSeries = {
                name: n,
                type: 'line',
                dataIndex: n + "-line",
                color: color,
                marker: { fillColor: color, lineWidth: 2, lineColor: color },
                zIndex: 1
            };
            var rangeSeries = {
                name: n + "-range",
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
    // gets the chart color
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
            },
            'wpttrendchart': {
                plMetricChange: 'onPLMetricChange'
            }
        }
    }
});