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
        this.callParent(arguments);

        this.getViewModel().getStore('histogramData').proxy.extraParams = {
            name: this.getPageName(),
            interval: '1d'
        };

        this.getViewModel().getStore('wptTrendTable').proxy.extraParams = {
            name: this.getPageName()
        };
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    scrollable: 'y',

    minWidth: 600,
    height: Ext.getBody().getViewSize().height - 100,

    items: [{
        xtype: 'wpttrendchart',
        title: 'WPT Trend - median'
    },{
        xtype: 'customtimingchart',
        hidden: true
    },{
        xtype: 'wpttrendgrid'
    }],

    listeners: {
        beforeclose: 'updateUrlTabState'
    }
});
