Ext.define('OppUI.model.loadtestDashboard.LoadTestChart', {
    extend: 'OppUI.model.Base',

    fields: [
        { data: 'data', mapping: 'data', type: 'auto' },
        { data: 'modelFields', mapping: 'modelFields', type: 'auto'},
        { data: 'series', mapping: 'series', type: 'auto' }
    ]
});