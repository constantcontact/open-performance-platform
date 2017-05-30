Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtrendreport',

    onHistogramDataLoaded: function(histogramData) {
        var metricStore, defaultStore, wptTrendGrid, defaultStoreData;

        metricStore = this.getView()
                .getViewModel()
                .getStore('median');
            
        defaultStore = this.getView()
                .getViewModel()
                .getStore('histogramData');

        if(!metricStore.getProxy().getData()) {
            defaultStoreData = defaultStore.getProxy().getReader().rawData;

            metricStore.getProxy().setData(defaultStoreData);
            metricStore.load();   
        } else {
            metricStore.reload();
        }

        this.getView().down('wpttrendchart').setStore(metricStore);
        this.getView().down('wpttrendchart').setTitle('WPT Trend - median');
        
    },
    
    updateUrlTabState: function(tab) {
        this.getView()
            .up('uxtabpanel')
            .getController()
            .updateUrlTabState(tab.getTitle(), false);
    }
});
 