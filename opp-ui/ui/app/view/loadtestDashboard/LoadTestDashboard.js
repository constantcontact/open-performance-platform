Ext.define('OppUI.view.loadtestDashboard.LoadTestDashboard',{
    extend: 'Ext.panel.Panel',
    xtype: 'loadtest',
    itemId: 'loadtest',

    requires: [
        'OppUI.view.loadtestDashboard.LoadTestDashboardController',
        'OppUI.view.loadtestDashboard.LoadTestDashboardModel'
    ],

    controller: 'loadtest',
    viewModel: {
        type: 'loadtest'
    },

    items: [{
        xtype: 'loadtestsummarytab',
        closable: false
    }],
    
    config: {
        activeState: null
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    },

    processQueryParams: function(params) {
        this.down('loadtestsummarytab').getController().createTabs(params);
        this.down('loadtestsummarytab').getController().processAdmin(params);
    }

});