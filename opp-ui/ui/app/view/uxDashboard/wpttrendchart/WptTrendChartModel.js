Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChartModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.wpttrendchart',
    data: {
        name: 'OppUI'
    },

    stores: {
        histogramDataFilter: {
            source: '{histogramData}'
        }
        // ,
        // min: {
        //     source: '{histogramData}',
        //     filters: [
        //         function(record) {
        //             return record.get('TTFB-min') || record.get('VisuallyComplete-min') || record.get('SpeedIndex-min') || undefined;
        //         }
        //     ]

        // }

    }

});
