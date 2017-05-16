
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
        textFilter: undefined,
        filters: undefined
    },

    initComponent: function() {
        var queryParams, i;
        this.callParent(arguments);

        i = 0;
        for(var prop in this.getFilters()) {
            if(this.getFilters().hasOwnProperty(prop)) {
                if(i > 0) {
                    queryParams += ('&' + prop + '=' + this.getFilters()[prop]);
                } else {
                    queryParams = prop + '=' + this.getFilters()[prop];
                }
                i++;
            }
        }

        console.log('Query Parameters: ' + queryParams);

        this.getViewModel()
            .getStore('groupReport')
            .getProxy()
            .setUrl('http://roadrunner.roving.com/loadsvc/v1/loadtests/all/summaryTrendByGroup?' 
                    //+ this.getColumnFilter() + '=' + this.getTextFilter());
                    + queryParams);
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
