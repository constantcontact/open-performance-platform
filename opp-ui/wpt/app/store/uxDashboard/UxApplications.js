Ext.define('OppUI.store.uxDashboard.UxApplications', {
    extend: 'Ext.data.Store',

    alias: 'store.uxapplications',
    model: 'OppUI.model.uxDashboard.UxApplications',
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
            //console.log("UxApplications Loaded from Store");
        }
    }
});