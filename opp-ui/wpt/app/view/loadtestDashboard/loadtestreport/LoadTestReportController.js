Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestreport',
    
    onRemoteChartLoaded: function(remoteData) {
        var view, items, references, respPct90, loadTestReportView, tSeriesYAxes, aggregateYAxes, chart;
        console.log("Remote chart data loaded!!");
        console.log(remoteData.getData().items[0].getData().series);

        view = this.getView();

        tSeriesYAxes = view.getChartTimeSeriesYAxes();

        for(var i = 0; i < tSeriesYAxes.length; i++ ) {
            chart = view.down('#' + tSeriesYAxes[i]);
            console.log(chart);
            chart.setTitle('90th Percentile Response Time During Test');

            chart.setSeries(remoteData.getData().items[0].getData().series);

            chart.setStore(Ext.create('Ext.data.JsonStore', {
                fields: remoteData.getData().items[0].getData().modelFields,
                data: remoteData.getData().items[0].getData().data
            }));
        }

        // respPct90 = view.down('#resp_pct90');
        // respPct90.setTitle('90th Percentile Response Time During Test');

        // respPct90.setSeries(remoteData.getData().items[0].getData().series);

        // respPct90.setStore(Ext.create('Ext.data.JsonStore', {
        //     fields: remoteData.getData().items[0].getData().modelFields,
        //     data: remoteData.getData().items[0].getData().data
        // }));
    },

    chartData: function(response) {
        var view, items, references, respPct90, loadTestReportView, tSeriesYAxes, aggregateYAxes, chart;
        console.log("Ajax Chart Data Returned!!");
        
        var json = Ext.decode(response.responseText, false);

        console.log(json);

        // view = this.getView();

        // tSeriesYAxes = view.getChartTimeSeriesYAxes();

        // for(var i = 0; i < tSeriesYAxes.length; i++ ) {
        //     chart = view.down('#' + tSeriesYAxes[i]);
        //     console.log(chart);
        //     chart.setTitle('90th Percentile Response Time During Test');

        //     chart.setSeries(remoteData.getData().items[0].getData().series);

        //     chart.setStore(Ext.create('Ext.data.JsonStore', {
        //         fields: remoteData.getData().items[0].getData().modelFields,
        //         data: remoteData.getData().items[0].getData().data
        //     }));
        // }
    }
});
