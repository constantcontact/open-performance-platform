Ext.define('OppUI.view.uxDashboard.apptrend.UxTrendReport',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.uxtrendreport',
    // xtype: 'apptrend',
    // itemId: 'apptrend',

    requires: [
        'OppUI.view.uxDashboard.apptrend.UxTrendReportController',
        'OppUI.view.uxDashboard.apptrend.UxTrendReportModel'
    ],

    controller: 'uxtrendreport',
    viewModel: {
        type: 'uxtrendreport'
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
