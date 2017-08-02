Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.wpttrendchart',

    requires: [
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController',
        'OppUI.view.uxDashboard.wpttrendchart.WptTrendChartModel'
    ],

    controller: 'wpttrendchart',
    viewModel: {
        type: 'wpttrendchart'
    },

    width: '100%',
    height: 500,
    title: 'Page Load Time Metrics',
    margin: '20 0 0 0',

    legend: {
        docked: 'right'
    },
    insetPadding: 40,

    tbar: {
        items: [
            '->',
            {
                xtype: 'button',
                itemId: 'medianButton',
                text: 'median',
                handler: 'buttonMetricClicked',
                cls: 'x-btn-selected'
            },
            '-',
            {
                xtype: 'button',
                text: 'average',
                handler: 'buttonMetricClicked'
            }
        ]
    },
    layout: 'fit',
    items: [{
        xtype: 'highcharts',
        series: [],
        height: 500,
        width: 700,
        store: Ext.create('Ext.data.Store', {
            fields: ['completed', 'min', 'max'],
            autoLoad: true,
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        }),
        chartConfig: {
            credits: { enabled: false },
            chart: {
                type: 'arearange',
                zoomType: 'x',
                marginLeft: 50,
                marginRight: 50
            },
            // rangeSelector: {
            //     selected: 1
            // },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: null
                }
            },
            tooltip: {
                crosshairs: true,
                shared: true,
                valueSuffix: 'ms'
            },
            title: {
                text: ''
            }
        }
    }]
});