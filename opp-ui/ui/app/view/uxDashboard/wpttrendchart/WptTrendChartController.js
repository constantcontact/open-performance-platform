Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.wpttrendchart',

    buttonMetricClicked: function(button) {
        console.log('Button Metric clicked ==>'+ button.getText());
        var metricsStore, defaultStore, wptTrendGrid;
        metricStore = this.getView()
                .up('uxtrendreport')
                .getViewModel()
                .getStore(button.getText());
            
        defaultStore = this.getView()
                .up('uxtrendreport')
                .getViewModel()
                .getStore('histogramData');

        wptTrendGrid = this.getView().up('uxtrendreport').down('wpttrendgrid');

        metricStore.getProxy().setData(defaultStore.getProxy().getData());
        metricStore.load();
        

        this.getView().setStore(metricStore);
        wptTrendGrid.setStore(metricStore);
    }

});
