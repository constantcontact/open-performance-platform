
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
        filters: undefined,
        url: ''
    },

    initComponent: function() {
        var queryParams, queryParam, i;
        
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

        this.getViewModel()
            .getStore('groupReport')
            .getProxy()
            .setUrl('/loadsvc/v1/loadtesttrends/summarytrendgroup?' + queryParams);
        // have to add this here in order to get the reportLink.
        this.items = [
            {
                xtype: 'loadtestgroupreportheader',
                reportLink: this.reportLink
            },{
                xtype: 'loadtestgroupreportgrid'
            }
        ],

        this.callParent(arguments);
    },

    listeners: {
        beforeclose: function(tab) {            
            this.up('loadtest')
                .getController()
                .updateUrlGroupTabState({name: tab.getTitle(), filters: tab.getFilters()}, false);
        }
    }
});
