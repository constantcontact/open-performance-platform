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
        scrollable: true
    },

    items: [
    {
        title: 'Load Tests',
        xtype: 'loadtestsummary'
    }],

    createTab: function(grid, record, item, index) {
        console.log("creating tab: " + grid + " " + record.getData() + " " + item + " " + index);

        //var tabPanel = this.getView(),
        var html = "Hello World!",
            tab = this.add({
                title: 'Test Run #' + record.getData().loadTestId,
                html: html
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