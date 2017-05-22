Ext.define('OppUI.view.uxDashboard.apptrend.AppTrend',{
    extend: 'Ext.panel.Panel',
    xtype: 'apptrend',
    itemId: 'apptrend',

    requires: [
        'OppUI.view.uxDashboard.apptrend.AppTrendController',
        'OppUI.view.uxDashboard.apptrend.AppTrendModel'
    ],

    controller: 'apptrend',
    viewModel: {
        type: 'apptrend'
    },

    config: {
        activeState: null,
        pageName: null
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },


    title: 'App Trend',
    scrollable: 'y',

    minWidth: 600,

    items: [
    {
        xtype: 'wpttrendchart',
        title: 'WPT Trend'
    },{
        xtype: 'wpttrendgrid'
       
    }],

    listeners: {
        beforeclose: function(tab) {
            console.log('tab closing ' + tab.getTitle());
            this.up('uxtabpanel').getController().updateUrlTabState(tab.getTitle(), false);
        }
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    }
});
