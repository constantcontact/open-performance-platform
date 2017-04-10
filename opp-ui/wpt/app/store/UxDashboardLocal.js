Ext.define('OppUI.store.UxDashboardLocal', {
    extend: 'Ext.data.Store',
    alias: 'store.uxdashboardlocal',
    model: 'OppUI.model.UxDashboard',
    autoLoad: false,

    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        },
        data: []
    }
});