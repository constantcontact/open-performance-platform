Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReport',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.uxtrendreport',
    xtype: 'uxtrendreport',

    requires: [
        'OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController',
        'OppUI.view.uxDashboard.uxtrendreport.UxTrendReportModel'
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

    scrollable: 'y',

    minWidth: 600,

    items: [
    {
        xtype: 'wpttrendchart',
        title: 'WPT Trend'
    },{
        xtype: 'wpttrendgrid',
        title: 'WPT Summary'
       
    }],

    listeners: {
        beforeclose: function(tab) {
            console.log('tab closing ' + tab.getTitle());
            this.up('uxtabpanel').getController().updateUrlTabState(tab.getTitle(), false);
        }
    }
});
