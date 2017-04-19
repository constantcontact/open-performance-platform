
Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.loadtestchart',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartController',
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartModel'
    ],

    config: {
        yAxis: undefined
    },

    // config: {
    //     loadTestId: 6528
    // },

    // initComponent: function() { 
    //     this.callParent(arguments);

    //     this.getViewModel()
    //         .getStore('remoteChart')
    //         .getProxy()
    //         .setUrl('http://roadrunner.roving.com/loadsvc/v1/charts/aggregate/loadtests/'+ this.getLoadTestId());

    //     console.log("LoadTestChart LoadTestId: " + this.getLoadTestId());
    // },

    controller: 'loadtestchart',
    viewModel: {
        type: 'loadtestchart'
    },

    // bind: { 
    //     store: '{resp_pct90}'
    // },

    axes: [
        {
            type: 'numeric',
            minimum: 0,
            position: 'left',
            title: 'Response Time (msec)', // gets overridden with yaxisTitle
            grid: true
        }, {
            type: 'category',
            position: 'bottom',
            fields: ['xaxis'],
            title: 'Run Date', // gets overridden with xaxisTitle
            renderer: function (v) {
                if (v.length > 20) {
                    this.font = "10px Arial, Helvetica, sans-serif";
                }
                return Ext.Date.format(new Date(v * 1000), 'm/d/y');
            }
        }
    ]
});
