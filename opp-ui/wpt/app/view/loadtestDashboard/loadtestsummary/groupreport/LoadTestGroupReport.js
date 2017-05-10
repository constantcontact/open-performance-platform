
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreport.LoadTestGroupReport',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.loadtestgroupreport',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreport.LoadTestGroupReportController',
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreport.LoadTestGroupReportModel'
    ],
    controller: 'loadtestgroupreport',
    viewModel: {
        type: 'loadtestgroupreport'
    },

    config: {
        columnFilter: undefined,
        textFilter: undefined
    },

    initComponent: function() {
        this.callParent(arguments);
        this.getViewModel()
            .getStore('groupReport')
            .getProxy()
            .setUrl('http://roadrunner.roving.com/loadsvc/v1/loadtests/all/summaryTrendByGroup?' 
                    + this.getColumnFilter() + '=' + this.getTextFilter());
    },

    items: [
        {
            xtype: 'container',
            html: '<p><b>TIP:</b> Double click on any row to drill down to that test run.</p>'
        },
        {
            xtype: 'loadtestgroupreportgrid'
        }
    ]
});
