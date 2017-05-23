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
        pageName: null,
        connection: null
    },

    initComponent: function() {
        this.callParent(arguments);
        this.getViewModel().getStore('histogramData').proxy.extraParams = {
            name: this.getPageName(),
            interval: '1d'
        }
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
        title: 'WPT Summary - median'
    }],

    listeners: {
        beforeclose: function(tab) {
            console.log('tab closing ' + tab.getTitle());
            this.up('uxtabpanel').getController().updateUrlTabState(tab.getTitle(), false);
        },
        histogramDataLoaded: function() {
            console.log('histogramDataLoaded');
        },
        afterrender: function(tab) {
            //tab.down('#medianButton').click();
        }
    }
});
