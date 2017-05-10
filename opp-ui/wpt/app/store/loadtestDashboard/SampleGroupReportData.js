Ext.define('OppUI.store.loadtestDashboard.SampleGroupReportData', {
    extend: 'Ext.data.Store',
    alias: 'store.apptrend',
    model: 'OppUI.model.uxDashboard.AppTrend',
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

        }
    }
});