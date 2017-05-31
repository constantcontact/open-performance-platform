Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.wpttrendchart',

    buttonMetricClicked: function(button) {
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
