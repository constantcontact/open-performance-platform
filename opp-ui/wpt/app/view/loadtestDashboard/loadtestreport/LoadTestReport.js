Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReport',{
    extend: 'Ext.panel.Panel',
    xtype: 'loadtestreport',
    alias: 'widget.loadtestreport',

    config: {
        title: 'Default title',
        loadTestId: undefined
    },
    
    initComponent: function() { 
        this.callParent(arguments);

        this.getViewModel()
            .getStore('remoteAggData')
            .getProxy()
            .setUrl('http://roadrunner.roving.com/loadsvc/v1/loadtests/'+ this.getLoadTestId() + '/aggdata');

        console.log("LoadTestReport LoadTestId: " + this.getLoadTestId());

        Ext.apply(this.items[2], {loadTestId: this.getLoadTestId()});
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
            scrollable: true,
            region: 'center',
            margin: '5 0 0 0',
            xtype: 'loadtestreportmain'
        }
    ]
});
