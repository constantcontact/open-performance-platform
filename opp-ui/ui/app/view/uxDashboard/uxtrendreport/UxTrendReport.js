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

    initComponent: function() {
    
        this.getViewModel().getStore('histogramData').proxy.extraParams = {
            name: this.getPageName(),
            interval: '1d'
        };

        this.getViewModel().getStore('wptTrendTable').proxy.extraParams = {
            name: this.getPageName()
        };

        this.callParent(arguments);
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    scrollable: 'y',

    minWidth: 600,

    items: [{
        xtype: 'wpttrendchart',
        title: 'WPT Trend - median'
    },{
        xtype: 'wpttrendgrid',
        title: 'WPT Summary - median',
    }],

    listeners: {
        beforeclose: function(tab) {
            console.log('tab closing ' + tab.getTitle());
            this.up('uxtabpanel').getController().updateUrlTabState(tab.getTitle(), false);
        }
    }
});
