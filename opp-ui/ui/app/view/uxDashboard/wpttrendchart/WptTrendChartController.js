Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.wpttrendchart',

    buttonMetricClicked: function(button) {
        // console.log('Button Metric clicked ==>'+ button.getText());
        var metricsStore, defaultStore, wptTrendGrid, defaultStoreData;
        metricStore = this.getView()
                .up('uxtrendreport')
                .getViewModel()
                .getStore(button.getText());
            
        defaultStore = this.getView()
                .up('uxtrendreport')
                .getViewModel()
                .getStore('histogramData');

        if(!metricStore.getProxy().getData()) {
            console.log('Loading ' + button.getText() + ' for the first time!');
            defaultStoreData = defaultStore.getProxy().getReader().rawData;
            
            metricStore.getProxy().setData(defaultStoreData);
            metricStore.load();   
        } else {
            metricStore.reload();
        }

        this.getView().setStore(metricStore);
        this.getView().up('uxtrendreport').down('wpttrendchart').setTitle('WPT Trend - ' + button.getText());
    }

});
