
Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.wpttrendchart',

    requires: [
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController',
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartModel'
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

    listeners: {
            itemclick: function(o) {
                //THIS SERIESINDEX POINTS TO WHICH FIELD WAS SELECTED
                //IF YOUR FIELDS ARE IN SOME ARRAY THIS WILL WORK NICELY -- I JUST WISH THERE WAS GOOD DOCUMENTATION ON THIS
                alert ( "Selected: " + o.seriesIndex );   
            var rec = store.getAt(o.index);
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
        marker: {
            type: 'square',
            fx: {
                duration: 200,
                easing: 'backOut'
            }
        },
        highlightCfg: {
            scaling: 2
        },
        tooltip: {
            trackMouse: true,
            renderer: function (tooltip, record, item) {
                var title = item.series.getTitle();

                if(record) {
                    tooltip.setHtml(title + ' on ' + record.get('timestamp') + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
        }
    },{
        type: 'line',
        title: 'Visually Complete',
        xField: 'wptTimestamp',
        yField: 'VisuallyComplete',
        marker: {
            type: 'triangle',
            fx: {
                duration: 200,
                easing: 'backOut'
            }
        },
        highlightCfg: {
            scaling: 2
        },
        tooltip: {
            trackMouse: true,
            renderer: function (tooltip, record, item) {
                var title = item.series.getTitle();

                if (record) {
                    tooltip.setHtml(title + ' on ' + record.get('timestamp') + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
        }     
    },{
        type: 'line',
        title: 'Speed Index',
        xField: 'wptTimestamp',
        yField: 'SpeedIndex',
        marker: {
            type: 'cross',
            fx: {
                duration: 200,
                easing: 'backOut'
            }
        },
        highlightCfg: {
            scaling: 2
        },
        tooltip: {
            trackMouse: true,
            renderer: function (tooltip, record, item) {
                var title = item.series.getTitle();

                if (record) {
                    tooltip.setHtml(title + ' on ' + record.get('timestamp') + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
        }
    }]
});
