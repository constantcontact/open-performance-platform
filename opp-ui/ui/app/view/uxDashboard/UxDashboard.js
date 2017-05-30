Ext.define('OppUI.view.uxDashboard.UxDashboard',{
    extend: 'Ext.panel.Panel',
    xtype: 'ux',
    itemId: 'ux',

    require:[
        'OppUI.view.uxDashboard.UxDashboardController',
        'OppUI.view.uxDashboard.UxDashboardModel'
    ],

    controller: 'uxdashboard',
    viewModel: {
        type: 'uxdashboard'
    },


    items: [{
        xtype: 'uxtabpanel',
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
        this.down('uxtabpanel').createTabs(params);
        this.down('uxtabpanel').processAdmin(params);
    }

});
