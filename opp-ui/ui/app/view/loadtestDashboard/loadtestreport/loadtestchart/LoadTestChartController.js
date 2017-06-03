Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestchart',

    onRemoteChartLoaded: function(remoteData) {
        var view, items;

        view = this.getView();
        view.setTitle('90th Percentile Response Time During Test');

        view.setSeries(remoteData.getData().items[0].getData().series);
        // Ext.apply(view, {series: remoteData.getData().items[0].getData().series});

        view.setStore(Ext.create('Ext.data.JsonStore', {
            fields: remoteData.getData().items[0].getData().modelFields,
            data: remoteData.getData().items[0].getData().data
        }));

        // Ext.apply(view, { store: Ext.create('Ext.data.JsonStore', {
        //         fields: remoteData.getData().items[0].getData().modelFields,
        //         data: remoteData.getData().items[0].getData().data
        //     })
        // });

    },

    updateChart: function(container, yaxis, json){    
        var series = json.chart.series;
    
        for(var i=0; i<series.length; i++){
            series[i].style = container.getSeriesStyle();
            //series[i].highlight = container.getSeriesHighlight();
            series[i].marker = container.getSeriesMarker();
            //series[i].tooltip = container.getSeriesTooltip();
        }

        container.axes[0].fields = json.chart.modelFields.slice(1);
        container.setTitle(json.chart.title);
        container.setSeries(series);
        container.setStore(Ext.create('Ext.data.JsonStore', {
            fields: json.chart.modelFields,
            data: json.chart.data
        }));
    },

    loadChart: function(yaxis, container) {
        var loadTestReport = container.up('loadtestreport');
        
        Ext.Ajax.request({
            url: '/loadsvc/v1/charts/timeseries/loadtests/'+ loadTestReport.getLoadTestId() + "?yaxis=" + yaxis,
            scope: this,
            disableCaching: false,
            success: function (response) {
                var json = Ext.decode(response.responseText, false);
                // callback to either update or create method
                this.updateChart(container, this.yaxis, json);
            }
        });
    }
});
