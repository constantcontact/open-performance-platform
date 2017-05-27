Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtrendreport',

    onHistogramDataLoaded: function(histogramData) {
        console.log('Histogram data loaded!!!');
        var metricStore, defaultStore, wptTrendGrid, defaultStoreData;

        metricStore = this.getView()
                .getViewModel()
                .getStore('median');
            
        defaultStore = this.getView()
                .getViewModel()
                .getStore('histogramData');

        if(!metricStore.getProxy().getData()) {
            console.log('Loading median for the first time!');
            defaultStoreData = defaultStore.getProxy().getReader().rawData;

            metricStore.getProxy().setData(defaultStoreData);
            metricStore.load();   
        } else {
            metricStore.reload();
        }

        this.getView().down('wpttrendchart').setStore(metricStore);
        this.getView().down('wpttrendchart').setTitle('WPT Trend - median');
        
    }
});
 