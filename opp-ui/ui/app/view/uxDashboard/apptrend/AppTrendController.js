Ext.define('OppUI.view.uxDashboard.apptrend.AppTrendController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.apptrend',

    init: function(view) {
        view.updateActiveState = this.updateActiveState.bind(this);
    },

    updateActiveState: function(activeState) {
        var refs = this.getReferences();
        var viewModel = this.getViewModel();

        //this.fireEvent('changeroute', this, 'ux/' + activeState);
    },

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
    }
    
});
 