
Ext.define('OppUI.view.loadtestDashboard.loadtestreport.timeseriescombo.TimeSeriesComboBox',{
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.timeseriescombobox',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestreport.timeseriescombo.TimeSeriesComboBoxController',
        'OppUI.view.loadtestDashboard.loadtestreport.timeseriescombo.TimeSeriesComboBoxModel'
    ],

    controller: 'timeseriescombobox',
    viewModel: {
        type: 'timeseriescombobox'
    },

    bind: {
        store: '{timesSeriesCombo}'
    },
    width:200,
    editable:false,
    displayField: "display",
    valueField: "value",
    fieldLabel: 'Show:',
    labelWidth:35,
    anchor: '0',
    queryMode: 'local',
    selectOnTab: true,
    name: 'chartType',
    listeners: {
        change: 'timeseriesYaxisChange'
    }
});
