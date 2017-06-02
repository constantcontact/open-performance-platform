
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
                    startTime = record.data.completedDate;
                    if(!startTime) {
                        startTime = window.parseInt(record.data.xaxis);
                    }
                    tooltip.setHtml(item.field + ' on ' + new Date(startTime) + ': ' +
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
    insetPadding: 40,

     tbar: {
        items: [
            '->',
            '-',
            {
                xtype: 'button',
                itemId: 'medianButton',
                text: 'median',
                handler: 'buttonMetricClicked'
            },
            '-',
            {
                xtype: 'button',
                text: 'average',
                handler: 'buttonMetricClicked'
            }
        ]
    },
    
    axes: [
        {
            type: 'numeric',
            minimum: 0,
            position: 'left',
            title: 'Response Time (msec)', // gets overridden with yaxisTitle
            grid: true
        }, {
            type: 'time',
            position: 'bottom',
            fields: ['completedDate'],
            title: 'Run Date', // gets overridden with xaxisTitle
            label: {
                rotate: {
                    degrees: -45
                }
            },
        }
    ]
});
