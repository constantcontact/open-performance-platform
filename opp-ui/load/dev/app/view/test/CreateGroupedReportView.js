
Ext.create('Ext.data.Store', {
    storeId:'reportTestData',
    pageSize:25,
    model: 'CCPerf.model.TestLoadModel',
    sortOnLoad: true,
    autoLoad:false,
    sorters: { property: 'startTime', direction : 'DESC' },
    proxy: {
        type: 'ajax',
        api: {
            read: '' // gets set on the fly
        },
        reader: {
            type: 'json'
        }
    }
});
Ext.define('CCPerf.view.test.CreateGroupedReportView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.create-grouped-report',
    width: 600,
    bodyPadding: 5,
    resizable: true,
    fieldDefaults: { anchor: '100%' },
    layout: {
        type: 'vbox', align: 'stretch'  // Child items are stretched to full width
    },
    items: [

        {
            xtype: 'panel', title: 'Report Details',
            items: [{xtype: 'textfield', fieldLabel: 'Name', labelWidth: 50, width: 400, name: 'report-name', padding:10}]
        },
        {
            title: 'Set filters (regex allowed)',
            xtype: 'panel',
            items:[
                { xtype: 'create-grouped-filter-field' }
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
                    text: 'Show Sample Data'
                },
                {
                    xtype: 'button',
                    text: 'Create Report',
                    itemId: "btnCreateGroupedReport",
                    padding: 5
                }]
        },
        {
            xtype: 'gridpanel',
            height:'auto',
            title: 'Sample Results',
            store: Ext.data.StoreManager.lookup('reportTestData'),
            columnLines: true,
            columns: [
                {text: 'TestId', dataIndex: "loadTestId", hidden: true},
                {text: 'Test Name', dataIndex: "testName"},
                {text: 'Sub Name', dataIndex: "testSubName", hidden: true},
                {text: 'Application', dataIndex: "appUnderTest"},
                {text: 'App Version', dataIndex: "appUnderTestVersion", hidden: true},
                {text: 'Comments', dataIndex: "comments", hidden: true},
                {text: 'Description', dataIndex: "description", hidden: true},
                {text: 'Environment', dataIndex: "environment"},
                {
                    text: 'Start Time', dataIndex: "startTime", renderer: function (v) {
                      return Ext.Date.format(new Date(v*1000), 'm/d/Y H:i a');
                    }
                },
                {text: 'Test Tool', dataIndex: "testTool", hidden: true},
                {text: 'Tool Version', dataIndex: "testToolVersion", hidden: true},
                {text: '# Users', dataIndex: "vuserCount"}
            ]
        }
    ]

});
