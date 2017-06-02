Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtrendreport',

    onHistogramDataLoaded: function(histogramData) {
        var metricStore, 
            defaultStore, 
            wptTrendGrid, 
            defaultStoreData,
            customTimingsStore,
            customTimingChart, 
            customTimingsChartData;

        metricStore = this.getView()
                .getViewModel()
                .getStore('median');
            
        defaultStore = this.getView()
                .getViewModel()
                .getStore('histogramData');

        customUserTimingsMedianStore = this.getView()
                .getViewModel()
                .getStore('customUserTimingsMedian');

        customUserTimingsAverageStore = this.getView()
                .getViewModel()
                .getStore('customUserTimingsAverage');
                

        if(!metricStore.getProxy().getData()) {
            defaultStoreData = defaultStore.getProxy().getReader().rawData;

            metricStore.getProxy().setData(defaultStoreData);
            metricStore.load();   
        } else {
            metricStore.reload();
        }

        // TODO: Remove this once the endpoint is ready.
        this.mockCustomTimings(histogramData.getData().items);

        customTimingsChartData = this.buildCustomTimingsChartData(histogramData.getData().items);

        if(customTimingsChartData.series.length > 0) {
            customTimingChart = this.getView().down('customtimingchart');

            for(var i=0; i<customTimingsChartData.series.length; i++){
                var yField = customTimingsChartData.series[i].yField;
                if(!(yField.indexOf('-min') >= 0 || yField.indexOf('-max') >= 0)) {
                    customTimingsChartData.series[i].style = customTimingChart.getSeriesStyle();
                    customTimingsChartData.series[i].highlightCfg = customTimingChart.getSeriesHighlight();
                    customTimingsChartData.series[i].marker = customTimingChart.getSeriesMarker();
                }
                customTimingsChartData.series[i].tooltip = customTimingChart.getSeriesTooltip();
                customTimingsChartData.series[i].showInLegend = 
                    !(yField.indexOf('-min') >= 0 || yField.indexOf('-max') >= 0);// || yField.indexOf('median') >= 0 || yField.indexOf('average') >= 0);
            }

            customUserTimingsMedianStore.getProxy().setData(customTimingsChartData.medianData);
            customUserTimingsMedianStore.setFields(customTimingsChartData.customTimingFields);

            customUserTimingsAverageStore.getProxy().setData(customTimingsChartData.averageData);
            customUserTimingsAverageStore.setFields(customTimingsChartData.customTimingFields);

            customTimingChart.setSeries(customTimingsChartData.series);
            customTimingChart.setStore(customUserTimingsMedianStore);

            customUserTimingsMedianStore.load();
            customUserTimingsAverageStore.load();

            customTimingChart.show();
            //customTimingChart.redraw();

            customTimingChart.setTitle('Custom Timings - median');
        }

        this.getView().down('wpttrendchart').setStore(metricStore);
        this.getView().down('wpttrendchart').setTitle('WPT Trend - median');
        
    },
    
    updateUrlTabState: function(tab) {
        this.getView()
            .up('uxtabpanel')
            .getController()
            .updateUrlTabState(tab.getTitle(), false);
    },

    buildCustomTimingsChartData: function(histogramData) {
        var record, 
            uniqueCustomTimingNames = {}, // = {min: undefined, max: undefined },
            customTimingFields = [],
            minName,
            maxName,
            customTimingName,
            customTimingMedianData,
            customTimingAverageData,
            series = [],
            medianData = [];
            averageData = [];

        for(var i = 0; i < histogramData.length; i++) {
            record = histogramData[i];
            if(record && record.userTimings) {
                for(var j = 0; j< record.userTimings.length; j++) {
                    
                    // keep track of the unique custom user timing names.
                    customTimingName = (record.userTimings[j].name);
                    uniqueCustomTimingNames[customTimingName] = undefined;

                    // build the data for the chart.
                    customTimingMedianData = new Object({});
                    minName = (customTimingName + '-min');
                    maxName = (customTimingName + '-max');
                    customTimingMedianData[customTimingName] = record.userTimings[j].median;
                    customTimingMedianData[minName] = record.userTimings[j].min;
                    customTimingMedianData[maxName] = record.userTimings[j].max;
                    customTimingMedianData['completedDate'] = record.getData().wptTimestamp;

                    medianData.push(customTimingMedianData);

                    customTimingAverageData = new Object({});
                    minName = (customTimingName + '-min');
                    maxName = (customTimingName + '-max');
                    customTimingAverageData[customTimingName] = record.userTimings[j].average;
                    customTimingAverageData[minName] = record.userTimings[j].min;
                    customTimingAverageData[maxName] = record.userTimings[j].max;
                    customTimingAverageData['completedDate'] = record.getData().wptTimestamp;

                    averageData.push(customTimingAverageData);
                }
            }
        }

        for(var prop in uniqueCustomTimingNames) {
            if(uniqueCustomTimingNames.hasOwnProperty(prop)) {
                customTimingFields.push(prop);
                
                series.push({
                    type: 'line',
                    title: prop,
                    xField: 'completedDate',
                    yField: prop
                });

                series.push({
                    type: 'line',
                    xField: 'completedDate',
                    yField: (prop + '-min'),
                    marker: {
                        type: 'cross'
                    }
                });

                series.push({
                    type: 'line',
                    xField: 'completedDate',
                    yField: (prop + '-max'),
                    marker: {
                        type: 'cross'
                    }
                });
            }
        }

        return { series: series, medianData: medianData, averageData: averageData, customTimingFields: customTimingFields };
    },

    mockCustomTimings: function(histogramData) {
        for(var i = 0; i < histogramData.length; i++) {
            record = histogramData[i];
            // record.userTimings = [{name: 'Custom Timing 1', average: 1000, max: 3000, median: 2000, min: 400},
            //     {name: 'Custom Timing 2', average: 2100, max: 4200, median: 1130, min: 540},
            //     {name: 'Custom Timing 3', average: 1100, max: 5200, median: 2030, min: 640},
            //     {name: 'Custom Timing 4', average: 2100, max: 6200, median: 1330, min: 740},
            //     {name: 'Custom Timing 5', average: 1100, max: 7200, median: 2430, min: 840}]

            record.userTimings = [{name: 'Custom Timing 1', average: Math.floor((Math.random() * 1000) + 1), max: Math.floor((Math.random() * 1000) + 1), median: Math.floor((Math.random() * 1000) + 1), min: Math.floor((Math.random() * 1000) + 1)},
                {name: 'Custom Timing 2', average: Math.floor((Math.random() * 1000) + 1), max: Math.floor((Math.random() * 1000) + 1), median: Math.floor((Math.random() * 1000) + 1), min: Math.floor((Math.random() * 1000) + 1)},
                {name: 'Custom Timing 3', average: Math.floor((Math.random() * 1000) + 1), max: Math.floor((Math.random() * 1000) + 1), median: Math.floor((Math.random() * 1000) + 1), min: Math.floor((Math.random() * 1000) + 1)},
                {name: 'Custom Timing 4', average: Math.floor((Math.random() * 1000) + 1), max: Math.floor((Math.random() * 1000) + 1), median: Math.floor((Math.random() * 1000) + 1), min: Math.floor((Math.random() * 1000) + 1)},
                {name: 'Custom Timing 5', average: Math.floor((Math.random() * 1000) + 1), max: Math.floor((Math.random() * 1000) + 1), median: Math.floor((Math.random() * 1000) + 1), min: Math.floor((Math.random() * 1000) + 1)}]
        }
    }
});
 