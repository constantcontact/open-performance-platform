
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
        store: '{median}'
    },

    width: '100%',
    height: 500,

    margin: '20 0 0 0',

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
            '-',
            {
                xtype: 'button',
                itemId: 'medianButton',
                text: 'median',
                listeners: {
                    afterrender: function(button) {
                        //button.click();
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
        fields: ['TTFB', 
                'TTFB-min', 
                'TTFB-max', 
                'VisuallyComplete',
                'VisuallyComplete-min',
                'VisuallyComplete-max',
                'SpeedIndex',
                'SpeedIndex-min',
                'SpeedIndex-max'
                ],
        position: 'left',
        grid: true,
        minimum: 0,
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
        type: 'scatter',
        title: 'TTFB-min',
        xField: 'wptTimestamp',
        yField: 'TTFB-min',
        marker: {
            type: 'cross'
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
        },
        showInLegend: false  
    },{
        type: 'scatter',
        title: 'TTFB-min',
        xField: 'wptTimestamp',
        yField: 'TTFB-max',
        marker: {
            type: 'cross'
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
        },
        showInLegend: false  
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
        type: 'scatter',
        title: 'VisuallyComplete-min',
        xField: 'wptTimestamp',
        yField: 'VisuallyComplete-min',
        marker: {
            type: 'cross'
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
        },
        showInLegend: false   
    },{
        type: 'scatter',
        title: 'VisuallyComplete-max',
        xField: 'wptTimestamp',
        yField: 'VisuallyComplete-max',
         marker: {
            type: 'cross'
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
        },
        showInLegend: false  
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
    },{
        type: 'scatter',
        title: 'SpeedIndex-min',
        xField: 'wptTimestamp',
        yField: 'SpeedIndex-min',
        marker: {
            type: 'cross'
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
        },
        showInLegend: false  
    },{
        type: 'scatter',
        title: 'SpeedIndex-min',
        xField: 'wptTimestamp',
        yField: 'SpeedIndex-max',
        marker: {
            type: 'cross'
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
        },
        showInLegend: false  
    }]
});
