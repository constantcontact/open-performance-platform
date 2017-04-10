Ext.define('OppUI.store.AppSummary', {
    extend: 'Ext.data.Store',
    alias: 'store.appsummary',
    model: 'OppUI.model.AppSummary',
    autoLoad: true,
    proxy: {
        type: 'ajax',
        reader: {
            type: 'json'
        },
        url: 'http://mysafeinfo.com/api/data?list=englishmonarchs&format=json'
    }
});