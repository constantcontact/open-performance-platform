Ext.define('OppUI.store.AppTrend', {
    extend: 'Ext.data.Store',
    alias: 'store.apptrend',
    model: 'OppUI.model.AppTrend',
    autoLoad: false,

    pageSize: 15,
    proxy: {
        type: 'memory',
        enablePaging: true,
        reader: {
            type: 'json'
        }
    },

    listeners: {
        load: function() {
            console.log("App Trend Loaded from Store");
        }
    }
});