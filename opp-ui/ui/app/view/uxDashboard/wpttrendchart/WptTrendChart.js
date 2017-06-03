
Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.wpttrendchart',

    requires: [
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController',
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartModel',
        'Ext.chart.series.Line',
        'Ext.chart.axis.Numeric',
        'Ext.chart.axis.Time',
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
    },

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

    axes: [{
        type: 'numeric',
        position: 'left',
        grid: true,
        minimum: 0,
        title: 'Response Time (msec)'
    },{
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
        marker: { radius: 4 }
        // highlight: true,
        // highlightCfg: {
        //     fillStyle: '#000', 
        //     radius: 5, 
        //     lineWidth: 2, 
        //     strokeStyle: '#fff'
        // }
        // ,
        // tooltip: {
        //     trackMouse: true,
        //     scope: this,
        //     renderer: function (tooltip, record, item) {
        //         // var title = item.series.getTitle();

        //         // if(record) {
        //         //     tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
        //         //         record.get(item.series.getYField()) + ' (ms)');
        //         // }
        //     }
        // }
    },{
        type: 'line',
        title: 'TTFB-min',
        xField: 'wptTimestamp',
        yField: 'TTFB-min',
        marker: {
            type: 'cross'
        },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     },
        showInLegend: false  
    },{
        type: 'line',
        title: 'TTFB-min',
        xField: 'wptTimestamp',
        yField: 'TTFB-max',
        marker: {
            type: 'cross'
        },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     },
        showInLegend: false  
    },{
        type: 'line',
        title: 'Visually Complete',
        xField: 'wptTimestamp',
        yField: 'VisuallyComplete',
        style: { lineWidth: 4 },
        marker: { radius: 4 }
    //     highlightCfg: {
    //         fillStyle: '#000', 
    //         radius: 5, 
    //         lineWidth: 2, 
    //         strokeStyle: '#fff'
    //     },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     }   
    },{
        type: 'line',
        title: 'VisuallyComplete-min',
        xField: 'wptTimestamp',
        yField: 'VisuallyComplete-min',
        marker: {
            type: 'cross'
        },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     },
        showInLegend: false   
    },{
        type: 'line',
        title: 'VisuallyComplete-max',
        xField: 'wptTimestamp',
        yField: 'VisuallyComplete-max',
         marker: {
            type: 'cross'
        },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     },
        showInLegend: false  
    },{
        type: 'line',
        title: 'Speed Index',
        xField: 'wptTimestamp',
        yField: 'SpeedIndex',
        style: { lineWidth: 4 },
        marker: { radius: 4 }
    //     highlightCfg: {
    //         fillStyle: '#000', 
    //         radius: 5, 
    //         lineWidth: 2, 
    //         strokeStyle: '#fff'
    //     },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     }
    },{
        type: 'line',
        title: 'SpeedIndex-min',
        xField: 'wptTimestamp',
        yField: 'SpeedIndex-min',
        marker: {
            type: 'cross'
        },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     },
        showInLegend: false  
    },{
        type: 'line',
        title: 'SpeedIndex-min',
        xField: 'wptTimestamp',
        yField: 'SpeedIndex-max',
        marker: {
            type: 'cross'
        },
    //     tooltip: {
    //         trackMouse: true,
    //         renderer: function (tooltip, record, item) {
    //             var title = item.series.getTitle();

    //             if (record) {
    //                 tooltip.setHtml(title + ' on ' + new Date(record.get('wptTimestamp')) + ': ' +
    //                     record.get(item.series.getYField()) + ' (ms)');
    //             }
    //         }
    //     },
        showInLegend: false  
    }]
});
