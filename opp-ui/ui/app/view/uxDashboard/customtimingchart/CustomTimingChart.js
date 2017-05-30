
Ext.define('OppUI.view.uxDashboard.customtimingchart.CustomTimingChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.customtimingchart',

    requires: [
        'OppUI.view.uxDashboard.customtimingchart.CustomTimingChartController',
        'OppUI.view.uxDashboard.customtimingchart.CustomTimingChartModel',
        'Ext.chart.plugin.ItemEvents'
    ],

    controller: 'customtimingchart',
    viewModel: {
        type: 'customtimingchart'
    },

    config: {
        seriesStyle: { lineWidth: 4 },
        seriesMarker: { radius: 4 },
        seriesHighlight: {
            fillStyle: '#000', 
            radius: 5, 
            lineWidth: 2, 
            strokeStyle: '#fff'
        },
        seriesTooltip: {
            trackMouse: true, 
            renderer: function (tooltip, record, item) {
                var startTime;
                if(item && record) {
                    // determine if the tooltip is for a timeseries chart
                    // or aggregation chart.
                    startTime = record.data.start_time;
                    if(!startTime) {
                        startTime = window.parseInt(record.data.xaxis);
                    }
                    tooltip.setHtml(item.field + ' on ' + new Date(startTime * 1000) + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
        }
    },

    plugins: {
        ptype: 'chartitemevents'
    },
    legend: {
        docked: 'right'
    },
    title: 'Custom Timings',
    height: 500,

     axes: [
        {
            type: 'numeric',
            minimum: 0,
            position: 'left',
            title: 'Response Time (msec)', // gets overridden with yaxisTitle
            grid: true
        }, {
            type: 'category',
            position: 'bottom',
            fields: ['start_time'],
            title: 'Run Date', // gets overridden with xaxisTitle
            renderer: function (o, value) {
                if (value.length > 20) {
                    this.font = "10px Arial, Helvetica, sans-serif";
                }
                //return parseInt(value) * 1000;
                return Ext.Date.format(new Date(parseInt(value) * 1000), 'm/d/y');
            }
        }
    ]
});
