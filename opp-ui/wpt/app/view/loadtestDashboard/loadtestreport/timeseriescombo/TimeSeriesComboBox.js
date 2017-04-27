
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
        change: function(view, selectedField, oldValue, eOpts ){
            var container = view.up('loadtestchart');

            if (selectedField === 'nPCT') {
                Ext.MessageBox.prompt('Enter Percentile', 'Integer Percentile value:',  function (btn, pctValue){
                    pctValue = pctValue.trim();
                    if(/^[0-9]+$/.test(String(pctValue))){
                        container.loadChart('resp_pct'+pctValue, container);
                    } else {
                        Ext.MessageBox.alert("Error","You must enter an integer value.");
                        selectedField.setValue(oldValue);
                    }

                });
            } else {
                container.loadChart(selectedField, container);
            }
        }
    }
});
