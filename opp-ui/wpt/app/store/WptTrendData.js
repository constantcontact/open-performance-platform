Ext.define('OppUI.store.WptTrendData', {
    extend: 'Ext.data.Store',
    alias: 'store.wpttrenddata',

    model: 'OppUI.model.AppTrend',
    autoLoad: false,

    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        },
        data: []
    },

    listeners: {
        load: function() {
            console.log("Charting App Trend Loaded from Store");
        }
    }
});