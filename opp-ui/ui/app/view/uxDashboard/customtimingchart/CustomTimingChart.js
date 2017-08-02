Ext.define('OppUI.view.uxDashboard.customtimingchart.CustomTimingChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.customtimingchart',

    requires: [
        'OppUI.view.uxDashboard.customtimingchart.CustomTimingChartController',
        'OppUI.view.uxDashboard.customtimingchart.CustomTimingChartModel'
    ],
    title: 'Custom User Timings',
    controller: 'customtimingchart',
    viewModel: {
        type: 'customtimingchart'
    },
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
            fields: ['timePeriod', 'min', 'max'],
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