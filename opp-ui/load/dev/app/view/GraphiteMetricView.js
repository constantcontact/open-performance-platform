Ext.define('CCPerf.view.GraphiteMetricView', {
    extend: 'Ext.container.Container',
    requires:[
        'Ext.selection.CellModel',
        'Ext.grid.*',
        'Ext.data.*',
        'Ext.util.*',
        'Ext.form.*',
        'CCPerf.view.graphiteMetric.GraphiteMetricGrid'
    ],
    id:'graphite-metric-view',
    padding:20,
    xtype: 'graphite-metric-view',
    items:  [
        {
            region: 'center',
            autoScroll: true,
            scope: this,
            margins: '5 0 0 0',
            items: [
                { xtype: 'graphite-metric-grid', title: 'Graphite Metrics'}
            ]
        }
    ]
});

