Ext.define('OppUI.store.loadtestDashboard.SampleGroupReportData', {
    extend: 'Ext.data.Store',
    alias: 'store.samplegroupreportdata',
    model: 'OppUI.model.loadTestDashboard.LoadTestReportSummary',
    autoLoad: false,

    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    },

    listeners: {
        load: function() {

        }
    }
});