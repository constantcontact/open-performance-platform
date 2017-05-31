Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReport', {
    extend: 'Ext.panel.Panel',
    xtype: 'loadtestreport',
    alias: 'widget.loadtestreport',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController',
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportModel',
        'Ext.layout.container.Border'
    ],

    config: {
        title: 'Default title',
        loadTestId: undefined,
        chartTimeSeriesYAxes: [
            { yaxis: 'resp_pct90', title: '90th Percentile Response Time During Test' },
            { yaxis: 'call_count', title: 'Transactions per Minute During Test' }
        ],
        chartAggregateYAxes: [
            { yaxis: 'resp_pct90', title: 'Trend: 90th Percentile Response Time' },
            { yaxis: 'resp_pct75', title: 'Trend: 75th Percentile Response Time' },
            { yaxis: 'resp_avg', title: 'Trend: Average Response Time' },
            { yaxis: 'resp_median', title: 'Trend: Median Response Time' },
            { yaxis: 'tps_median', title: 'Trend: TPS Median' },
            { yaxis: 'tps_max', title: 'Trend: TPS Max' }
        ]
    },
    defaults: {
        collapsible: true,
        split: false,
        bodyPadding: 10,
        scrollable: true
    },
    margin: '5 0 0 0',

    initComponent: function() {
        var i, me;

        me = this;
        me.callParent(arguments);

        me.getViewModel()
            .getStore('remoteAggData')
            .getProxy()
            .setUrl('/loadsvc/v1/loadtests/'+ me.getLoadTestId() + '/aggData');

        me.getViewModel()
            .getStore('remoteSlas')
            .getProxy()
            .setUrl('/loadsvc/v1/loadtests/'+ me.getLoadTestId() + '/slas');


        me.getViewModel()
            .getStore('remoteLoadTestInfo')
            .getProxy()
            .setUrl('/loadsvc/v1/loadtests/' + me.getLoadTestId());

        for(i = 0; i < me.getChartTimeSeriesYAxes().length; i++) {
            Ext.Ajax.request({
                url: '/loadsvc/v1/charts/timeseries/loadtests/'+ me.getLoadTestId() + '?yaxis=' + me.getChartTimeSeriesYAxes()[i].yaxis,
                scope: me,
                success: 'chartData'
            });
        }

        for(i = 0; i < me.getChartAggregateYAxes().length; i++) {
            Ext.Ajax.request({
                url: '/loadsvc/v1/charts/aggregate/loadtests/' + me.getLoadTestId() + "?yaxis=" + me.getChartAggregateYAxes()[i].yaxis,
                scope: me,
                success: 'chartData'
            });
        }
    },

    closable: true,
    
    layout: 'border',

    controller: 'loadtestreport',
    viewModel: {
        type: 'loadtestreport'
    },

    items: [
        {
            title: 'SLAs',
            xtype: 'loadtestsla',
            region: 'north',
            height: 100,
            minHeight: 300,
            maxHeight: 500,
            collapsed: true
        },
        {
            title: 'Load Test Details',
            region:'west',
            floatable: false,
            margin: '5 0 0 0',
            width: 125,
            minWidth: 300,
            maxWidth: 400,
            collapsed: true,
            tpl:[
                '<table class=\'tbl-load-test-details\'>',
                    '<tr>',
                        '<th>Id</th>',
                        '<td>{loadTestId}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Test Name</th>',
                        '<td>{testName}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Sub Name</th>',
                        '<td>{testSubName}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Vuser Count</th>',
                        '<td>{vuserCount}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Environment</th>',
                        '<td>{environment}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Application Tested</th>',
                        '<td>{appUnderTest}</td>',
                    '</tr>',
                    '<tr>',
                       '<th>Application Version</th>',
                        '<td>{appUnderTestVersion}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Start Time</th>',
                        '<td>{startTime}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>End Time</th>',
                        '<td>{endTime}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Test Tool</th>',
                        '<td>{testTool}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Test Tool Version:</th>',
                        '<td>{testToolVersion}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Description</td>',
                        '<td>{description}</td>',
                    '</tr>',
                    '<tr>',
                        '<th>Comments</th>',
                        '<td>{comments}</td>',
                    '</tr>',
                '</table>'
            ],
            bind: {
                data: {
                    loadTestId: '{loadTestId}',
                    testName: '{testName}',
                    testSubName: '{testSubName}',
                    vuserCount: '{vuserCount}',
                    environment: '{environment}',
                    appUnderTest: '{appUnderTest}',
                    appUnderTestVersion: '{appUnderTestVersion}',
                    startTime: '{startTime}',
                    endTime: '{endTime}',
                    testTool: '{testTool}',
                    testToolVersion: '{testToolVersion}',
                    description: '{description}',
                    comments: '{comments}'
                }
            }
        },
        {
            xtype: 'loadtestreportmain',
            collapsible: false,
            region: 'center',
            margin: '5 0 0 0'
        }
    ],

    listeners: {    
        beforeclose: 'beforeLoadTestReportClose'
    },
    
    chartData: function(response, options) {
       this.getController().chartData(response, options);
    }
});
