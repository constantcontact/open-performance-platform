
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarytab.LoadTestSummaryTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.loadtestsummarytab',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarytab.LoadTestSummaryTabController',
        'OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarytab.LoadTestSummaryTabModel',
        'Ext.ux.TabReorderer'
    ],

    controller: 'loadtestsummarytab',
    viewModel: {
        type: 'loadtestsummarytab'
    },

    plugins: 'tabreorderer',

    config: {
        scrollable: true,
        closable: true
    },

    items: [{
        title: 'Load Tests',
        xtype: 'loadtestsummary',
        iconCls: 'x-fa fa-table',
        reorderable: false,
        closable: false,
        scrollable: true,
        layout: 'fit'
    }],

    createTab: function(grid, record, item, index) {
        var tab;

        tab = this.add({
                closable: true,
                xtype: 'loadtestreport',
                iconCls: 'x-fa fa-line-chart',
                loadTestId: record.getData().loadTestId,
                title: 'Test Run #' + record.getData().loadTestId,
                scrollable: false
            }
        );
        
        this.setActiveTab(tab);
    },

    createGroupReportTab: function(groupReportName, columnFilter, textFilter) {
        var tab;

        tab = this.add({
            closable: true,
            xtype: 'loadtestgroupreport',
            iconCls: 'x-fa fa-line-chart',
            title: groupReportName,
            columnFilter: columnFilter,
            textFilter: textFilter
        });

        this.setActiveTab(tab);
    }
});
