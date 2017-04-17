Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReport',{
    extend: 'Ext.panel.Panel',
    //extend: 'Ext.tab.Panel',
    xtype: 'loadtestreport',
    alias: 'widget.loadtestreport',

    config: {
        title: 'Default title',
        loadTestId: 0
    },
    initComponent: function() { 
        this.callParent(arguments);
         console.log("Load Test Id " + this.getLoadTestId());
    },
    closable: true,
    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController',
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportModel',
        'Ext.layout.container.Border'
    ],

    layout: 'border',

    controller: 'loadtestreport',
    viewModel: {
        type: 'loadtestreport'
    },

    defaults: {
        collapsible: true,
        split: true,
        bodyPadding: 10
    },

    items: [
        {
            title: 'SLAs',
            region: 'north',
            height: 100,
            minHeight: 75,
            maxHeight: 150,
            html: '<p>Header content</p>'
        },
        {
            title: 'Load Test Details',
            region:'west',
            floatable: false,
            margin: '5 0 0 0',
            width: 125,
            minWidth: 100,
            maxWidth: 250,
            html: '<p>Secondary content like navigation links could go here</p>'
        },
        {
            collapsible: false,
            region: 'center',
            margin: '5 0 0 0',
            html: '<h2>Main Page</h2><p>This is where the main content would go</p>'
        }
    ]
});
