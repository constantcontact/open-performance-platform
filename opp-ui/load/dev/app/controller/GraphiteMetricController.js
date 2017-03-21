Ext.define('CCPerf.controller.GraphiteMetricController', {
    extend: 'Ext.app.Controller',
    requires: [
        'Ext.grid.View',
        'Ext.grid.column.Column',
        'Ext.form.field.ComboBox',
        'Ext.toolbar.Toolbar',
        'Ext.toolbar.Fill',
        'Ext.button.Button',
        'Ext.grid.plugin.RowEditing'
    ],
    stores: [
    ],
    models: [
        'GraphiteMetricModel',
        'ApplicationMapping'
    ],
    views: [
        'graphiteMetric.GraphiteMetricGrid'
    ]
});