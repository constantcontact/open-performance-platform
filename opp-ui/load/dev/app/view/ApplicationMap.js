Ext.define('CCPerf.view.ApplicationMap', {
    extend: 'Ext.container.Container',
    requires:[
        'Ext.selection.CellModel',
        'Ext.grid.*',
        'Ext.data.*',
        'Ext.util.*',
        'Ext.form.*',
        'CCPerf.view.applicationMapping.AppMapGrid'
    ],
    id:'app-map-wrap',
    padding:20,
    xtype: 'app-map',
    items:  [
        {
            region: 'center',
            autoScroll: true,
            scope: this,
            margins: '5 0 0 0',
            items: [
                { xtype: 'app-map-grid', title: 'Application Mapping'}
            ]
        }
    ]
});

