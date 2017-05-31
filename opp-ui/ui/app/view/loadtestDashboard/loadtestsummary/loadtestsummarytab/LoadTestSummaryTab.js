
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarytab.LoadTestSummaryTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.loadtestsummarytab',
    cls: 'loadtest-summary-tab',

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
    height: Ext.getBody().getViewSize().height,

    items: [{
        title: 'Load Tests',
        xtype: 'loadtestsummary',
        iconCls: 'x-fa fa-table',
        reorderable: false,
        closable: false
    }]
});
