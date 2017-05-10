Ext.define('OppUI.view.loadtestDashboard.LoadTestDashboard',{
    extend: 'Ext.tab.Panel',
    xtype: 'loadtest',
    itemId: 'loadtest',

    requires: [
        'Ext.ux.TabReorderer'
    ],

    controller: 'loadtest',
    viewModel: {
        type: 'loadtest'
    },

    plugins: 'tabreorderer',

    defaults: {
        bodyPadding: 10,
        scrollable: true,
        closable: true
    },

    items: [{
        title: 'Load Tests',
        xtype: 'loadtestsummary',
        iconCls: 'x-fa fa-table',
        reorderable: false,
        closable: false
    }],

    createTab: function(grid, record, item, index) {
        var tab;

        tab = this.add({
                closable: true,
                xtype: 'loadtestreport',
                iconCls: 'x-fa fa-line-chart',
                loadTestId: record.getData().loadTestId,
                title: 'Test Run #' + record.getData().loadTestId
                
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
    },
    

    config: {
        activeState: null,
        defaultActiveState: 'dashboard'
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    }

});