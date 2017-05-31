Ext.define('OppUI.view.loadtestDashboard.loadtestreport.timeseriescombo.TimeSeriesComboBoxController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.timeseriescombobox',

    timeseriesYaxisChange: function(view, selectedField, oldValue, eOpts ){
        var container = view.up('loadtestchart');

        if (selectedField === 'nPCT') {
            Ext.MessageBox.prompt('Enter Percentile', 'Integer Percentile value:',  function (btn, pctValue){
                pctValue = pctValue.trim();
                if(/^[0-9]+$/.test(String(pctValue))){
                    container.getController().loadChart('resp_pct'+pctValue, container);
                } else {
                    Ext.MessageBox.alert("Error","You must enter an integer value.");
                    selectedField.setValue(oldValue);
                }

            });
        } else {
            container.getController().loadChart(selectedField, container);
        }
    }
    
});
