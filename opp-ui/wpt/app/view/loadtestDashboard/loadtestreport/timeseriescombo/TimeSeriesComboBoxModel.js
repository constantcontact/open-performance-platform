Ext.define('OppUI.view.loadtestDashboard.loadtestreport.timeseriescombo.TimeSeriesComboBoxModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.timeseriescombobox',
    data: {
        name: 'OppUI'
    },

    stores: {
        timesSeriesCombo: {
            autoLoad: true,
            fields: [ "display","value" ],
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                },
                data: [{"display":'Min', "value":'resp_min'}, {"display":'Average', "value":'resp_avg'},
                        {"display":'Median', "value":'resp_median'}, {"display":'Max', "value":'resp_max'},
                        {"display":'75th PCT', "value":'resp_pct75'}, {"display":'90th PCT', "value":'resp_pct90'},
                        {"display":'nth PCT', "value":'nPCT'},
                        {"display":'Transaction count', "value":'call_count'}, {"display":'Error count', "value":'error_count'},
                        {"display":'Std. Dev.', "value":'resp_stddev'}, {"display":'Total Bytes Rec.', "value":'total_bytes_received'},
                        {"display":'Total Bytes Sent', "value":'total_bytes_sent'}, {"display":'Total Bytes', "value":'total_bytes'}
                    ]
            },
        }
    }
});
