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
        scrollable: true,
        activeState: null,
        defaultActiveState: 'dashboard'
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    },

    processQueryParams: function(params) {
        this.down('loadtestsummarytab').createTabs(params);
    }

});