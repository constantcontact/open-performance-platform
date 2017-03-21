Ext.define('CCPerf.store.ReportLoadSlaStore', {
    extend: 'Ext.data.Store',
     model: 'CCPerf.model.ReportLoadSlaModel',
    autoLoad: false,
    sortOnLoad: true,
    sorters: { property: 'start_time', direction : 'DESC' },
    proxy: {
        type: 'ajax',
        api: {
            read: '/loadsvc/v1/sla/loadtest/all/'
        },
        reader: {
            type: 'json',
            rootProperty: 'results',
            successProperty: 'success'
        }
    }
});