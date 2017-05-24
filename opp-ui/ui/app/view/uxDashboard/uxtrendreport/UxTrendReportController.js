Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtrendreport',

    onRemoteAppTrendLoad: function(remoteWPTTrendData) {
        var chartStore, items, view, gridData;
        console.log("On Remote App Trend Loaded!!");

        gridData = [];
        view = this.getView();
        
        items = remoteWPTTrendData.getData().items;

        // add to grid local stores.
        // this.getView().store.add(items);

        // add to the local store.
        for(var i = 0; i < items.length; i++) {
            gridData.push(items[i].getData());
        }
        view.store.getProxy().setData(gridData);
        view.store.load();

        // add to the chart local store.
        chartStore = Ext.ComponentQuery.query("#wptTrendChart")[0].getStore();
        // reset the data before adding to the store. 
        chartStore.proxy.data = [];
        chartStore.load();

        // reverse the data to ascending order.
        for (var i = items.length - 1; i >= 0; i--) {
            //items[i].getData().timestamp = new Date(items[i].getData().timestamp).getTime();
            chartStore.add(items[i]);
        }
    },

    onHistogramDataLoaded: function(histogramData) {
        console.log('Histogram data loaded!!!');
        var metricStore, defaultStore, wptTrendGrid, defaultStoreData;

        metricStore = this.getView()
                .getViewModel()
                .getStore('median');
            
        defaultStore = this.getView()
                .getViewModel()
                .getStore('histogramData');

        
        wptTrendGrid = this.getView().down('wpttrendgrid');

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
        
        wptTrendGrid.setStore(metricStore);
        wptTrendGrid.setTitle('WPT Summary - median' );
    }
});
 