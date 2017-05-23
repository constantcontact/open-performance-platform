
Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.wpttrendchart',

    requires: [
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController',
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartModel',
        'Ext.chart.plugin.ItemEvents'
    ],

    controller: 'wpttrendchart',
    viewModel: {
        type: 'wpttrendchart'
    },

    bind: {
        store: '{histogramDataFilter}'
    },

    width: '100%',
    height: 500,

    margin: '20px 20px 0 20px',

    legend: {
        docked: 'right'
    },
    insetPadding: 40,
    plugins: {
        ptype: 'chartitemevents'
        // moveEvents: true
    },

    listeners: {
        itemdblclick: function(series, item, event, eOpts ) {
            console.log('itemdblclicked');
        }
    },

    tbar: {
        items: [
            '->',
            {
                xtype: 'button',
                text: 'min',
                handler: 'buttonMetricClicked'
            },
            '-',
            {
                xtype: 'button',
                text: 'max',
                handler: 'buttonMetricClicked'
            },
            '-',
            {
                xtype: 'button',
                text: 'median',
                listeners: {
                    afterrender: function(button) {
                        button.click();
                    }
                },
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

    axes: [{
        type: 'numeric',
        fields: ['TTFB', 'VisuallyComplete', 'SpeedIndex'],
        position: 'left',
        grid: true,
        minimum: 0,
        // renderer: function (axis, label, layoutContext) {
        //     return label.toFixed(label < 10 ? 1: 0);
        // },
        title: "Milliseconds"
    }, {
        type: 'time',
        fields: 'wptTimestamp',
        position: 'bottom',
        label: {
            rotate: {
                degrees: -45
            }
        },
        title: 'Run Date'
    }],
    series: [{
        type: 'line',
        title: 'TTFB',
        xField: 'wptTimestamp',
        yField: 'TTFB',
        style: { lineWidth: 4 },
        marker: { radius: 4 },
        highlightCfg: {
            // scaling: 2
            fillStyle: '#000', 
            radius: 5, 
            lineWidth: 2, 
            strokeStyle: '#fff'
        },
        tooltip: {
            trackMouse: true,
            renderer: function (tooltip, record, item) {
                var title = item.series.getTitle();

                if(record) {
                    tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
        }
    },{
        type: 'line',
        title: 'Visually Complete',
        xField: 'wptTimestamp',
        yField: 'VisuallyComplete',
        style: { lineWidth: 4 },
        marker: { radius: 4 },
        highlightCfg: {
            // scaling: 2
            fillStyle: '#000', 
            radius: 5, 
            lineWidth: 2, 
            strokeStyle: '#fff'
        },
        tooltip: {
            trackMouse: true,
            renderer: function (tooltip, record, item) {
                var title = item.series.getTitle();

                if (record) {
                    tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
        }     
    },{
        type: 'line',
        title: 'Speed Index',
        xField: 'wptTimestamp',
        yField: 'SpeedIndex',
        style: { lineWidth: 4 },
        marker: { radius: 4 },
        highlightCfg: {
            // scaling: 2
            fillStyle: '#000', 
            radius: 5, 
            lineWidth: 2, 
            strokeStyle: '#fff'
        },
        tooltip: {
            trackMouse: true,
            renderer: function (tooltip, record, item) {
                var title = item.series.getTitle();

                if (record) {
                    tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
        }
    }]
});
