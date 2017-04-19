Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReport',{
    extend: 'Ext.panel.Panel',
    xtype: 'loadtestreport',
    alias: 'widget.loadtestreport',

    config: {
        title: 'Default title',
        loadTestId: undefined,
        chartTimeSeriesYAxes: [
            'resp_pct90'
            ,
            'call_count',
        ],
        chartAggregateYAxes: [
            'resp_pct90',
            'resp_pct75',
            'resp_avg',
            'resp_median',
            'tps_median',
            'tps_max'
        ]
    },
    
    initComponent: function() {
        var respPct90Chart, me;
        me = this;
        me.callParent(arguments);

        me.getViewModel()
            .getStore('remoteAggData')
            .getProxy()
            .setUrl('http://roadrunner.roving.com/loadsvc/v1/loadtests/'+ this.getLoadTestId() + '/aggdata');

        // this.getViewModel()
        //     .getStore('remoteChart')
        //     .getProxy()
        //     .setUrl('http://roadrunner.roving.com/loadsvc/v1/charts/aggregate/loadtests/'+ this.getLoadTestId());


        for(var i = 0; i < me.getChartTimeSeriesYAxes().length; i++) {
            Ext.Ajax.request({
                url: 'http://roadrunner.roving.com/loadsvc/v1/charts/aggregate/loadtests/' + me.getLoadTestId() + "?yaxis=" + this.getChartTimeSeriesYAxes()[i],
                async: true,
                scope: me,
                success: 'chartData'
            });
        }

        
        console.log("LoadTestReport LoadTestId: " + me.getLoadTestId());
    },

    chartData: function(response, options) {
        var view, items, references, respPct90, loadTestReportView, yaxis, aggregateYAxes, chart;
        console.log("Ajax Chart Data Returned!!");
        
        var json = Ext.decode(response.responseText, false);

        console.log(json);
        console.log(response);

        yaxis = options.url.substring(options.url.indexOf("=")).slice(1);

        chart = this.down('#' + yaxis);
        console.log(chart);
        chart.setTitle('90th Percentile Response Time During Test');

        chart.setSeries(json.chart.series);

        chart.setStore(Ext.create('Ext.data.JsonStore', {
            fields: json.chart.modelFields,
            data: json.chart.data
        }));
        
    },

    closable: true,
    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController',
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportModel',
        'Ext.layout.container.Border'
    ],

    layout: 'border',

    controller: 'loadtestreport',
    viewModel: {
        type: 'loadtestreport'
    },

    defaults: {
        collapsible: true,
        split: true,
        bodyPadding: 10
    },

    items: [
        {
            title: 'SLAs',
            region: 'north',
            height: 100,
            minHeight: 75,
            maxHeight: 150,
            html: '<p>Header content</p>'
        },
        {
            title: 'Load Test Details',
            region:'west',
            floatable: false,
            margin: '5 0 0 0',
            width: 125,
            minWidth: 100,
            maxWidth: 250,
            html: '<p>Secondary content like navigation links could go here</p>'
        },
        {
            xtype: 'loadtestreportmain',
            collapsible: false,
            scrollable: true,
            region: 'center',
            margin: '5 0 0 0'
        }
    ]
});
