Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestreport',


    remoteLoadTestInfoLoaded: function(loadTestInfo) {
        var viewModel, urTime, startTime, endTime, duration, cloudTestLink, externalTestId;
        viewModel = this.getViewModel();


        startTime = loadTestInfo.getData().items[0].getData().startTime;
        endTime = loadTestInfo.getData().items[0].getData().endTime;
        curTime = (typeof startTime === 'number' && startTime % 1 === 0) ? 
                    Ext.Date.format(new Date(startTime *1000),'m/d/Y h:i a') : startTime;
        duration = this.calculateDuration(startTime, endTime);
        externalTestId = loadTestInfo.getData().items[0].getData().externalTestId;

        if(externalTestId && externalTestId !== "") {
            cloudTestLink = 'http://cloudtest.roving.com/concerto/?initResultsTab=' + externalTestId;
        } else {
            cloudTestLink = "N/A";
        }
        
        // Set all the bindings for the load test report.
        viewModel.set('testName',  loadTestInfo.getData().items[0].getData().testName);
        viewModel.set('curTime',  curTime ? curTime : "");
        viewModel.set('duration',  duration);
        viewModel.set('vuserCount',  loadTestInfo.getData().items[0].getData().vuserCount);
        viewModel.set('environment',  loadTestInfo.getData().items[0].getData().environment);
        viewModel.set('description',  loadTestInfo.getData().items[0].getData().description);
        viewModel.set('cloudTestLink',  cloudTestLink);
        viewModel.set('loadTestId', loadTestInfo.getData().items[0].getData().loadTestId);
        viewModel.set('testSubName', loadTestInfo.getData().items[0].getData().testSubName);
        viewModel.set('appUnderTest', loadTestInfo.getData().items[0].getData().appUnderTest);
        viewModel.set('appUnderTestVersion', loadTestInfo.getData().items[0].getData().appUnderTestVersion);
        viewModel.set('startTime', Ext.Date.format(new Date(loadTestInfo.getData().items[0].getData().startTime *1000),'m/d/Y h:i a'));
        viewModel.set('endTime', Ext.Date.format(new Date(loadTestInfo.getData().items[0].getData().endTime *1000),'m/d/Y h:i a'));
        viewModel.set('testTool', loadTestInfo.getData().items[0].getData().testTool);
        viewModel.set('testToolVersion', loadTestInfo.getData().items[0].getData().testToolVersion);
        viewModel.set('comments', loadTestInfo.getData().items[0].getData().comments);
    },

    remoteSlasLoaded: function() {

    },

    calculateDuration: function(start, end){
        var duration = "";
        if(end !== null) {
            var durationSec = end/1000 - start/1000; // get the diff in sec
            if(durationSec > 86400){
                duration = Math.floor(durationSec/86400) + ' d ' + Math.round((durationSec % 86400)/60/60) + ' h'; // calculate hours and minutes
            }
            else if(durationSec > 3600){
                duration = Math.floor(durationSec/3600) + ' h ' + Math.round((durationSec % 3600)/60) + ' m'; // calculate hours and minutes
            } else {
                duration = Math.floor(durationSec/60) + ' m ' + durationSec % 60 + ' s'; // calculate minutes and seconds
            }
        }
        return duration;
    },

    chartData: function(response, options) {
        var json, yaxis, chart, title, itemPrepend, item, series, type, view;

        json = Ext.decode(response.responseText, false);
        series = json.chart.series;
        yaxis = options.url.substring(options.url.indexOf("=")).slice(1);
        type = options.url.indexOf('timeseries') >= 0 ? 'timeseries-' : 'trend-';
        view = this.getView();


        chart = view.down('#' + type + yaxis);

        if(chart) {
            for(var i=0; i<series.length; i++){
                series[i].style=chart.getSeriesStyle();
                //series[i].highlight=chart.getSeriesHighlight();
                series[i].marker=chart.getSeriesMarker();
                //series[i].tooltip=chart.getSeriesTooltip();
            }

            chart.axes[0].fields = json.chart.modelFields.slice(1);
            if(type.indexOf('timesseries') >= 0) {
                chart.axes[1].fields = ['start_time'];
            } else {
                chart.axes[1].fields = ['xaxis'];
            }
            chart.setSeries(series);
            chart.setStore(Ext.create('Ext.data.JsonStore', {
                fields: json.chart.modelFields,
                data: json.chart.data
            }));
            chart.redraw();  
        }
    },

    beforeLoadTestReportClose: function(tab) {
        this.getView().up('loadtest').getController().updateUrlTabState(tab.getLoadTestId(), false);
    }
});
