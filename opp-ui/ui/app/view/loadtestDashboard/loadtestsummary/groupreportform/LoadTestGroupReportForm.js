
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreportform.LoadTestGroupReportForm',{
    extend: 'Ext.form.Panel',
    alias: 'widget.loadtestgroupreportform',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreportform.LoadTestGroupReportFormController',
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreportform.LoadTestGroupReportFormModel'
    ],

    controller: 'loadtestgroupreportform',
    viewModel: {
        type: 'loadtestgroupreportform'
    },
    width: 600,
    bodyPadding: 5,
    resizable: true,
    fieldDefaults: { anchor: '100%' },
    layout: {
        type: 'vbox', align: 'stretch'  // Child items are stretched to full width
    },
    items: [{
            xtype: 'panel', title: 'Report Details',
            items: [{
                xtype: 'textfield',
                itemId: 'groupReportName', 
                fieldLabel: 'Name', 
                labelWidth: 50, 
                width: 400, 
                name: 'report-name', 
                padding:10
            }]
        },
        {
            title: 'Set filters (regex allowed)',
            xtype: 'panel',
            items:[
                { xtype: 'groupreportfilter' }
            ]
        },
        {
            xtype: 'fieldcontainer',
            fieldDefaults: { padding: 5 },
            items: [
                {
                    xtype: 'button',
                    itemId: 'btnShowGroupedSampleData',
                    padding: 5,
                    text: 'Show Sample Data',
                    margin: '5 5 5 0',
                    listeners: {
                        click: 'showSampleData'
                    } 
                },
                {
                    xtype: 'button',
                    text: 'Create Report',
                    itemId: "btnCreateGroupedReport",
                    padding: 5,
                    margin: '5 5 5 0',
                    listeners: {
                        click: 'createReport'
                    }
                }]
        },
        {
            xtype: 'grid',
            height:'auto',
            title: 'Sample Results',
            // store: Ext.data.StoreManager.lookup('reportTestData'),
             bind: {
                store: '{remoteSummaryTrendFilter}'
            },
            columnLines: true,
            columns: [
                {text: 'TestId', dataIndex: "load_test_id", hidden: true, flex: 1},
                {text: 'Test Name', dataIndex: "test_name", flex: 1},
                {text: 'Sub Name', dataIndex: "test_sub_name", hidden: true, flex: 1},
                {text: 'Application', dataIndex: "app_under_test", flex: 1},
                {text: 'App Version', dataIndex: "app_under_test_version", hidden: true, flex: 1},
                {text: 'Comments', dataIndex: "comments", hidden: true, flex: 1},
                {text: 'Description', dataIndex: "description", hidden: true, flex: 1},
                {text: 'Environment', dataIndex: "environment", flex: 1},
                {
                    text: 'Start Time', 
                    dataIndex: "start_time", 
                    renderer: function (v) {
                        return Ext.Date.format(new Date(v*1000), 'm/d/Y H:i a')
                    }, flex: 1
                },
                {text: 'Test Tool', dataIndex: "test_tool", hidden: true, flex: 1},
                {text: 'Tool Version', dataIndex: "test_tool_version", hidden: true, flex: 1},
                {text: '# Users', dataIndex: "vuser_count", flex: 1}
            ]
        }
    ]
});
