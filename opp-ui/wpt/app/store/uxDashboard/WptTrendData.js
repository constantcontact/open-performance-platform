Ext.define('OppUI.store.uxDashboard.WptTrendData', {
    extend: 'Ext.data.Store',
    alias: 'store.wpttrenddata',

    model: 'OppUI.model.uxDashboard.AppTrend',
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