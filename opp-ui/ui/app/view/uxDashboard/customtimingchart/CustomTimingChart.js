Ext.define('OppUI.view.uxDashboard.customtimingchart.CustomTimingChart', {
    extend: 'Ext.panel.Panel',
    //extend: 'Chart.ux.Highcharts',
    alias: 'widget.customtimingchart',

    requires: [
        'OppUI.view.uxDashboard.customtimingchart.CustomTimingChartController',
        'OppUI.view.uxDashboard.customtimingchart.CustomTimingChartModel' //,
        // 'Ext.chart.series.Line',
        // 'Ext.chart.axis.Numeric',
        // 'Ext.chart.axis.Time',
        // 'Ext.chart.plugin.ItemEvents'
    ],

    controller: 'customtimingchart',
    viewModel: {
        type: 'customtimingchart'
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
                text: 'Custom User Timings'
            }
        }
    }]

});