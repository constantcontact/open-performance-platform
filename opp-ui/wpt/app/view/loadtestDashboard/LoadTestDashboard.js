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
        reorderable: false,
        closable: false
    }],

    createTab: function(grid, record, item, index) {
        console.log("creating tab: " + grid + " " + record.getData() + " " + item + " " + index);
        var tab;

        tab = this.add(
            {
                closable: true,
                xtype: Ext.create('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReport', 
                    { 
                        title: 'Test Run #' + record.getData().loadTestId,
                        loadTestId: record.getData().loadTestId
                    })
            }
        );
        
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